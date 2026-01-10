package logicControllers;

import DAO.BillDAO;
import DAO.ReservationDAO;
import DAO.TableDAO;
import Entities.Bill;
import util.PaymentResult;

public class PaymentController {

    private final BillDAO billdb;
    private final ReservationDAO resdb;
    private final TableDAO tabdb;

    public PaymentController() {
        this.billdb = new BillDAO();
        this.resdb = new ReservationDAO();
        this.tabdb = new TableDAO();
    }

    public PaymentResult addPayment(Bill bill) {
        // בדיקה אם כבר שולמה
        Bill existingBill = billdb.getBillByReservationId(bill.getReservationID());
        if (existingBill != null && existingBill.isPaid()) {
            return new PaymentResult(false, "Payment cancelled: this reservation has already been paid.", null);
        }

        double originalAmount = bill.getTotalAmount();
        double finalAmount = originalAmount;
        boolean isSubscriber = false;

        try {
            isSubscriber = resdb.isSubscriberByReservationId(bill.getReservationID());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isSubscriber) {
            finalAmount = originalAmount * 0.9; // 10% הנחה
            finalAmount = Math.round(finalAmount * 100.0) / 100.0;
            bill.setTotalAmount(finalAmount);
        }

        try {
            boolean added = billdb.AddBill(bill);
            if (added) {
                try {
                	tabdb.updateTableIsFree(bill.getReservationID());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String msg = "Payment processed successfully.";
                if (isSubscriber) {
                    msg += " Original amount: " + originalAmount + ", after 10% discount: " + finalAmount;
                }
                return new PaymentResult(true, msg, bill);
            } else {
                return new PaymentResult(false, "Payment failed", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentResult(false, "Payment failed due to an internal error.", null);
        }
    }
}
