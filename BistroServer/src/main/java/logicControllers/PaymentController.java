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
        System.out.println("resid = " + bill.getReservationID());
        boolean isSubscriber = db.isSubscriberByReservationId(bill.getReservationID());

        if (isSubscriber) {
        	System.out.println("is SUBBBB");
            finalAmount = originalAmount * 0.9;
            System.out.println("fin" + finalAmount + " ori" +originalAmount );
            bill.setTotalAmount(finalAmount);
        }

        try {
            boolean added = db.AddBill(bill);
            if (added) {
            	try {
            		db.updateTableIsFree(bill.getReservationID());
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
