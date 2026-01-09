package logicControllers;

import DB.DBController;
import Entities.Bill;
import Entities.Reservation;
import Entities.Table;
import enums.ReservationStatus;
import util.PaymentResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Executes periodic daily operations on reservations such as:
 * - Marking no-shows
 * - Generating bills after dining time
 * - Cancelling expired waitlist reservations
 */
public class DailyFunctionController extends TimerTask {

    private static Timer timer = new Timer(true);
    private static final Object lock = new Object();

    private final DBController db = new DBController();
    private final PaymentController paymentController = new PaymentController();

    /**
     * Starts the daily timer task.
     * The task runs once every minute.
     */
    public static void startDailyTimer() {
        timer.scheduleAtFixedRate(
                new DailyFunctionController(),
                0,
                TimeUnit.MINUTES.toMillis(1)
        );
    }

    /**
     * Stops the running timer and resets it.
     */
    public static void stopTimer() {
        timer.cancel();
        timer = new Timer(true);
    }

    /**
     * Executes periodic reservation maintenance logic.
     */
    @Override
    public void run() {
        synchronized (lock) {
            try {
                List<Reservation> reservations = db.readAllReservations();
                LocalDateTime now = LocalDateTime.now();

                for (Reservation r : reservations) {

                    LocalDateTime reservationDateTime =
                            LocalDateTime.of(r.getReservationDate(), r.getReservationTime());

                    if (r.getStatus() == ReservationStatus.CONFIRMED &&
                            now.isAfter(reservationDateTime.plusMinutes(15))) {

                        r.setStatus(ReservationStatus.NOT_SHOWED);

                        if (r.getTableID() != null) {
                            Table table = db.GetTable(r.getTableID());
                            if (table != null) {
                                table.release();
                                db.UpdateTable(table);
                            }
                            r.setTableID(null);
                        }

                        db.updateReservation(r);
                    }

                    if (r.getStatus() == ReservationStatus.SEATED &&
                            r.getActualArrivalTime() != null &&
                            r.getBillID() == null) {

                        LocalDateTime actualArrivalDateTime =
                                LocalDateTime.of(r.getReservationDate(), r.getActualArrivalTime());

                        if (now.isAfter(actualArrivalDateTime.plusHours(2))) {

                            Bill bill = new Bill(0, r.getReservationID(), generateRandomAmount());
                            PaymentResult result = paymentController.addPayment(bill);

                            if (result.isSuccess() && result.getBill() != null) {
                                r.setBillID(result.getBill().getBillID());
                                r.setStatus(ReservationStatus.COMPLETED);
                                db.updateReservation(r);
                            }
                        }
                    }

                    if (r.getStatus() == ReservationStatus.WAITLIST &&
                            r.getTableID() == null &&
                            now.isAfter(reservationDateTime)) {

                        r.setStatus(ReservationStatus.CANCELLED);
                        db.updateReservation(r);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates a random bill amount between 100 and 1000.
     *
     * @return rounded bill amount
     */
    private double generateRandomAmount() {
        double amount = 100 + Math.random() * 900;
        return Math.round(amount * 100.0) / 100.0;
    }
}
