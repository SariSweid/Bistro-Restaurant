package logicControllers;

import DB.DBController;
import Entities.Reservation;
import Entities.Table;
import Entities.Bill;
import enums.ReservationStatus;
import util.PaymentResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * DailyFunctionController manages automatic restaurant operations:
 * - Cancel no-show reservations after 15 minutes.
 * - Generate bills 2 hours after seating.
 * - Free tables after payment.
 */
public class DailyFunctionController extends TimerTask {

    private static Timer thisTimer = new Timer();
    private final DBController db = new DBController();
    private final PaymentController paymentController = new PaymentController();

    /**
     * Schedule a debug run 1 second after calling.
     */
    public static void setTimerNowForDebug() {
        thisTimer.schedule(new DailyFunctionController(), 1000);
    }

    /**
     * Stop the daily timer.
     */
    public static void stopTimer() {
        thisTimer.cancel();
        thisTimer = new Timer();
    }

    /**
     * Main loop: checks reservations and handles cancellation and billing.
     */
    @Override
    public void run() {
        List<Reservation> reservations = db.readAllReservations();
        LocalDateTime now = LocalDateTime.now();

        for (Reservation r : reservations) {
            LocalDateTime reservationDateTime = LocalDateTime.of(r.getReservationDate(), r.getReservationTime());

            
            System.out.println("THE TIME IS:" + reservationDateTime);
            // 1. Cancel no-show reservations after 15 minutes
            if (r.getStatus() == ReservationStatus.CONFIRMED &&
                    now.isAfter(reservationDateTime.plusMinutes(15))) {

                r.setStatus(ReservationStatus.CANCELLED);

                if (r.getTableID() != null) {
                    Table t = db.GetTable(r.getTableID());
                    if (t != null) {
                        t.release();
                        db.UpdateTable(t);
                    }
                    r.setTableID(null);
                }

                db.updateReservation(r);
            }



            // 2. Generate bill 2 hours after seating
            if (r.getStatus() == ReservationStatus.CONFIRMED && r.getBillID() == 0 && r.getReservationTime() != null && now.isAfter(reservationDateTime.plusHours(2))) {

                try {
                   System.out.println("IM HERE");
                    Bill bill = new Bill(0, r.getReservationID(), generateRandomAmount());

                    
                    PaymentResult result = paymentController.addPayment(bill);

                    if (result.isSuccess() && result.getBill() != null) {
                        r.setBillID(result.getBill().getBillID());
                        db.updateReservation(r);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Generates a random amount for the bill (100-1000)
     */
    private double generateRandomAmount() {
        double amount = 100 + Math.random() * 900;
        return Math.round(amount * 100.0) / 100.0;
    }

    /**
     * Starts the daily timer to run every minute.
     */
    public static void startDailyTimer() {
        thisTimer.scheduleAtFixedRate(new DailyFunctionController(), 0, TimeUnit.MINUTES.toMillis(1));
    }
}
