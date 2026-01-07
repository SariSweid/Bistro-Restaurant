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

public class DailyFunctionController extends TimerTask {

    private static Timer thisTimer = new Timer(true);
    private final DBController db = new DBController();
    private final PaymentController paymentController = new PaymentController();

    public static void startDailyTimer() {
        thisTimer.scheduleAtFixedRate(
                new DailyFunctionController(),
                0,
                TimeUnit.MINUTES.toMillis(1)
        );
    }

    public static void stopTimer() {
        thisTimer.cancel();
        thisTimer = new Timer(true);
    }

    @Override
    public void run() {
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
                        Table t = db.GetTable(r.getTableID());
                        if (t != null) {
                            t.release();
                            db.UpdateTable(t);
                        }
                        r.setTableID(null);
                    }

                    db.updateReservation(r);
                }

                if (r.getStatus() == ReservationStatus.SEATED &&
                        r.getBillID() == 0 &&
                        now.isAfter(reservationDateTime.plusHours(2))) {

                    Bill bill = new Bill(0, r.getReservationID(), generateRandomAmount());
                    r.setStatus(ReservationStatus.COMPLETED);

                    PaymentResult result = paymentController.addPayment(bill);
                    if (result.isSuccess() && result.getBill() != null) {
                        r.setBillID(result.getBill().getBillID());
                        db.updateReservation(r);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double generateRandomAmount() {
        double amount = 100 + Math.random() * 900;
        return Math.round(amount * 100.0) / 100.0;
    }
}
