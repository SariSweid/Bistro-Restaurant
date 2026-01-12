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

    private static final int CHECK_INTERVAL_MS = 60 * 1000; // 1 minute
    private static final Object lock = new Object();

    private boolean isRunning = false;
    private Thread workerThread;

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final TableDAO tableDAO = new TableDAO();
    private final BillDAO billDAO = new BillDAO();
    private final WaitingListDAO waitingListDAO = new WaitingListDAO();
    private final PaymentController paymentController = new PaymentController();



    public void start() {
        if (!isRunning) {
            isRunning = true;
            workerThread = new Thread(this, "DailyFunctionWatchdog");
            workerThread.start();
            System.out.println("DailyFunctionController started");
        }
    }

    public void stop() {
        isRunning = false;
        System.out.println("DailyFunctionController stopped");
    }


    @Override
    public void run() {
        while (isRunning) {
            try {
                synchronized (lock) {
                    handleNoShows();
                    handleWaitingList();
                    cancelExpiredWaitingListEntries();
                    handleBillGeneration();
                }

                Thread.sleep(CHECK_INTERVAL_MS);

            } catch (InterruptedException e) {
                isRunning = false;
            } catch (Exception e) {
                System.err.println(" Error: " + e.getMessage());
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
        if (entry == null)
            return;

        if (entry.getExitReason() == ExitReason.CANCELLED ||
            entry.getExitReason() == ExitReason.SEATED)
            return;

        Table availableTable =
                tableDAO.findAvailableTable(entry.getNumOfGuests());

        if (availableTable == null)
            return;

        Reservation reservation = new Reservation(
                0,
                entry.getUserID(),
                entry.getNumOfGuests(),
                entry.getConfirmationCode(),
                entry.getWaitDate(),
                entry.getWaitTime(),
                ReservationStatus.CONFIRMED
        );

        reservation.setTableID(availableTable.getTableID());
        reservationDAO.insertReservation(reservation);

        availableTable.occupy();
        tableDAO.UpdateTable(availableTable);

        waitingListDAO.updateExitReasonByConfirmationCode(
                entry.getConfirmationCode(),
                ExitReason.SEATED
        );
    }

    private void cancelExpiredWaitingListEntries() {
        List<WaitingListEntry> waitingList =
                waitingListDAO.getWaitingEntriesWithoutExitReason();

        LocalDateTime now = LocalDateTime.now();

        for (WaitingListEntry entry : waitingList) {
            LocalDateTime entryDateTime =
                    LocalDateTime.of(entry.getWaitDate(), entry.getWaitTime());

            if (entryDateTime.isBefore(now)) {
                waitingListDAO.updateExitReasonByConfirmationCode(
                        entry.getConfirmationCode(),
                        ExitReason.CANCELLED
                );
            }
        }
    }

    private void handleBillGeneration() {
    	
        List<Reservation> reservations = reservationDAO.readAllReservations();
        LocalDateTime now = LocalDateTime.now();

        for (Reservation r : reservations) {
        	System.out.println("ID=" + r.getReservationID());
        	System.out.println("STATUS=" + r.getStatus());
        	System.out.println("ARRIVAL=" + r.getActualArrivalTime());
        	System.out.println("BILL=" + r.getBillID());

        	
            if (r.getStatus() != ReservationStatus.SEATED ||
                r.getActualArrivalTime() == null ||
                r.getBillID() != null)
            	
                continue;

            LocalDateTime arrivalDateTime =
                    LocalDateTime.of(r.getReservationDate(), r.getActualArrivalTime());

            if (now.isAfter(arrivalDateTime.plusHours(2))) {
                Bill bill = new Bill(0, r.getReservationID(), generateRandomAmount());
                PaymentResult result = paymentController.addPayment(bill);

                if (result.isSuccess() && result.getBill() != null) {
                    r.setBillID(result.getBill().getBillID());
                    r.setStatus(ReservationStatus.COMPLETED);
                    reservationDAO.updateReservation(r);
                }
            }
        }
    }

    private double generateRandomAmount() {
        double amount = 100 + Math.random() * 900;
        return Math.round(amount * 100.0) / 100.0;
    }
}
