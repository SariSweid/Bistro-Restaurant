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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class DailyFunctionController extends TimerTask {

    private static Timer timer = new Timer(true);
    private static final Object lock = new Object();

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final TableDAO tableDAO = new TableDAO();
    private final BillDAO billDAO = new BillDAO();
    private final WaitingListDAO waitingListDAO = new WaitingListDAO();
    private final PaymentController paymentController = new PaymentController();

    public static void startDailyTimer() {
        timer.scheduleAtFixedRate(
                new DailyFunctionController(),
                0,
                TimeUnit.MINUTES.toMillis(1)
        );
    }

    public static void stopTimer() {
        timer.cancel();
        timer = new Timer(true);
    }

    @Override
    public void run() {
        synchronized (lock) {
            try {
                handleNoShows();
                handleWaitingList();
                handleBillGeneration();
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
        while (true) {
            WaitingListEntry entry = waitingListDAO.getNextWaitingEntry();
            if (entry == null) break;

            Table availableTable = tableDAO.findAvailableTable(entry.getNumOfGuests());
            if (availableTable == null) break;

            
            Reservation reservation = new Reservation(
                    0, 
                    entry.getUserID() != null ? entry.getUserID() : 0, 
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

        
            waitingListDAO.updateExitReason(0, null);
        }
    }


    private void handleBillGeneration() {
        List<Reservation> reservations = reservationDAO.readAllReservations();
        LocalDateTime now = LocalDateTime.now();

        for (Reservation r : reservations) {
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
