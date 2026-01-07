package logicControllers;

import DB.DBController;
import Entities.Bill;
import util.PaymentResult;

public class PaymentController {

    private final DBController db;

    public PaymentController() {
        this.db = new DBController();
    }

    public PaymentResult addPayment(Bill bill) {
    	
        Bill existingBill = db.getBillByReservationId(bill.getReservationID());
        if (existingBill != null && existingBill.isPaid()) {
            return new PaymentResult(false, "Payment cancelled: this reservation has already been paid.", null);
        }

        double originalAmount = bill.getTotalAmount();
        double finalAmount = originalAmount;
        boolean isSubscriber = db.isSubscriberByReservationId(bill.getReservationID());

        if (isSubscriber) {
            finalAmount = originalAmount * 0.9;
            finalAmount = Math.round(finalAmount * 100.0) / 100.0; 
            bill.setTotalAmount(finalAmount);
        }

        try {
            boolean added = db.AddBill(bill);
            if (added) {
            	try {
            		db.updateTableIsFree(bill.getReservationID());
            		System.out.println("succes1");
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
