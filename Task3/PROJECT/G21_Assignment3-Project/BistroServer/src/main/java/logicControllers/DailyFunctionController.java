// DailyFunctionController.java
package logicControllers;

import DAO.BillDAO;
import DAO.ReservationDAO;
import DAO.TableDAO;
import DAO.WaitingListDAO;
import Entities.Bill;
import Entities.Reservation;
import Entities.Table;
import Entities.WaitingListEntry;
import enums.ExitReason;
import enums.ReservationStatus;
import util.PaymentResult;

import java.time.LocalDateTime;
import java.util.List;

public class DailyFunctionController implements Runnable {

    private static final int CHECK_INTERVAL_MS = 60 * 1000;
    private static final Object lock = new Object();
    private boolean isRunning = false;
    private Thread workerThread;

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final TableDAO tableDAO = new TableDAO();
    private final BillDAO billDAO = new BillDAO();
    private final WaitingListDAO waitingListDAO = new WaitingListDAO();
    private final PaymentController paymentController = new PaymentController();
    private final TableController tableController = new TableController();

    public void start() {
        if (!isRunning) {
            isRunning = true;
            workerThread = new Thread(this, "DailyFunctionWatchdog");
            workerThread.start();
        }
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                synchronized (lock) {
                    handleNoShows();
                    handleWaitingList();
                    cancelExpiredWaitingListEntries();
                    handleSeatedReservations();
                }
                Thread.sleep(CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                isRunning = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleNoShows() {
        List<Reservation> reservations = reservationDAO.readAllReservations();
        LocalDateTime now = LocalDateTime.now();

        for (Reservation r : reservations) {
            if (r.getStatus() != ReservationStatus.CONFIRMED)
                continue;

            LocalDateTime reservationDateTime =
                    LocalDateTime.of(r.getReservationDate(), r.getReservationTime());

            if (now.isAfter(reservationDateTime.plusMinutes(15))) {
                r.setStatus(ReservationStatus.NOT_SHOWED);

                if (r.getTableID() != null) {
                    Table table = tableDAO.GetTable(r.getTableID());
                    if (table != null) {
                        table.release();
                        tableDAO.UpdateTable(table);
                    }
                    r.setTableID(null);
                }

                reservationDAO.updateReservation(r);
            }
        }
    }

    private void handleWaitingList() {
        WaitingListEntry entry = waitingListDAO.getNextWaitingEntry();
        if (entry == null) return;
        if (entry.getExitReason() == ExitReason.CANCELLED || entry.getExitReason() == ExitReason.SEATED) return;

        List<Table> allTables = tableController.getAllTables();
        List<Reservation> reservations = reservationDAO.getConfirmedReservationsAt(entry.getWaitDate(), entry.getWaitTime());

        boolean[] tableOccupied = new boolean[allTables.size()];
        for (Reservation r : reservations) {
            for (int i = 0; i < allTables.size(); i++) {
                Table t = allTables.get(i);
                if (!tableOccupied[i] && t.getCapacity() >= r.getNumOfGuests()) {
                    tableOccupied[i] = true;
                    break;
                }
            }
        }

        boolean canSeat = false;
        for (int i = 0; i < allTables.size(); i++) {
            Table t = allTables.get(i);
            if (!tableOccupied[i] && t.getCapacity() >= entry.getNumOfGuests()) {
                canSeat = true;
                break;
            }
        }

        if (!canSeat) return;

        Integer uid = entry.getUserID();
        Reservation reservation = new Reservation(
                0,
                uid == null ? 0 : uid,
                entry.getNumOfGuests(),
                entry.getConfirmationCode(),
                entry.getWaitDate(),
                entry.getWaitTime(),
                ReservationStatus.CONFIRMED,
                true
        );

        reservationDAO.insertReservation(reservation);
        waitingListDAO.updateExitReasonByConfirmationCode(entry.getConfirmationCode(), ExitReason.SEATED);
    }

    private void cancelExpiredWaitingListEntries() {
        List<WaitingListEntry> waitingList = waitingListDAO.getWaitingEntriesWithoutExitReason();
        LocalDateTime now = LocalDateTime.now();

        for (WaitingListEntry entry : waitingList) {
            LocalDateTime entryDateTime = LocalDateTime.of(entry.getWaitDate(), entry.getWaitTime());
            if (entryDateTime.isBefore(now)) {
                waitingListDAO.updateExitReasonByConfirmationCode(entry.getConfirmationCode(), ExitReason.CANCELLED);
            }
        }
    }

    private void handleSeatedReservations() {
        List<Reservation> reservations = reservationDAO.readAllReservations();
        LocalDateTime now = LocalDateTime.now();

        System.out.println("[handleSeatedReservations] Checking " + reservations.size() + " reservations at " + now);

        for (Reservation r : reservations) {
            System.out.println("[handleSeatedReservations] Reservation ID: " + r.getReservationID() +
                               ", status: " + r.getStatus() +
                               ", actualArrivalTime: " + r.getActualArrivalTime());

            if (r.getStatus() != ReservationStatus.SEATED || r.getActualArrivalTime() == null) {
                System.out.println("[handleSeatedReservations] Skipping reservation, not SEATED or no arrival time");
                continue;
            }

            LocalDateTime arrivalDateTime = LocalDateTime.of(r.getReservationDate(), r.getActualArrivalTime());

            if (now.isAfter(arrivalDateTime.plusHours(2))) {
                System.out.println("[handleSeatedReservations] Reservation ID " + r.getReservationID() + " passed 2 hours, generating bill and completing");

                r.setPaymentReminderSent(true);
                System.out.println("[handleSeatedReservations] Payment reminder flag set to true");

                Bill bill = new Bill(0, r.getReservationID(), generateRandomAmount());
                PaymentResult result = paymentController.addPayment(bill);
                if (result.isSuccess() && result.getBill() != null) {
                    r.setBillID(result.getBill().getBillID());
                    System.out.println("[handleSeatedReservations] Bill created with ID: " + r.getBillID());
                }

                r.setStatus(ReservationStatus.COMPLETED);
                System.out.println("[handleSeatedReservations] Status set to COMPLETED");

                if (r.getTableID() != null) {
                    Table table = tableDAO.GetTable(r.getTableID());
                    if (table != null) {
                        table.release();
                        tableDAO.UpdateTable(table);
                        System.out.println("[handleSeatedReservations] Table " + table.getTableID() + " released");
                    }
                    r.setTableID(null);
                }

                reservationDAO.updateReservation(r);
                System.out.println("[handleSeatedReservations] Reservation updated in database");
            } else {
                System.out.println("[handleSeatedReservations] Reservation ID " + r.getReservationID() + " has not yet reached 2 hours");
            }
        }
    }


    private double generateRandomAmount() {
        double amount = 100 + Math.random() * 900;
        return Math.round(amount * 100.0) / 100.0;
    }
}
