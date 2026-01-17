package logicControllers;

import DAO.BillDAO;
import DAO.ReservationDAO;
import DAO.TableDAO;
import Entities.Bill;
import util.PaymentResult;

/**
 * Controller class responsible for handling the payment process within the restaurant.
 * It manages bill creation, calculates discounts for subscribers, and updates 
 * table availability upon successful payment.
 */
public class PaymentController {

    /** Data access object for bill-related database operations. */
    private final BillDAO billdb;
    
    /** Data access object for reservation-related database operations. */
    private final ReservationDAO resdb;
    
    /** Data access object for table-related database operations. */
    private final TableDAO tabdb;

    /**
     * Initializes a new PaymentController and instantiates the required DAOs 
     * for bills, reservations, and tables.
     */
    public PaymentController() {
        this.billdb = new BillDAO();
        this.resdb = new ReservationDAO();
        this.tabdb = new TableDAO();
    }

    /**
     * Processes a payment for a specific bill. 
     * This method checks if the bill was already paid, applies a 10% discount if the 
     * customer is a subscriber, saves the bill to the database, and frees the 
     * associated table.
     *
     * @param bill The Bill entity containing payment details such as amount and reservation ID.
     * @return A PaymentResult object indicating success or failure, a descriptive message, 
     * and the updated Bill object.
     */
    public PaymentResult addPayment(Bill bill) {
        
        // Check if a paid bill already exists for this reservation
        Bill existingBill = billdb.getBillByReservationId(bill.getReservationID());
        if (existingBill != null && existingBill.isPaid()) {
            return new PaymentResult(false, "Payment cancelled: this reservation has already been paid.", null);
        }

        double originalAmount = bill.getTotalAmount();
        double finalAmount = originalAmount;
        boolean isSubscriber = false;

        // Determine if the customer associated with the reservation is a subscriber
        try {
            isSubscriber = resdb.isSubscriberByReservationId(bill.getReservationID());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Apply 10% discount for subscribers and round to 2 decimal places
        if (isSubscriber) {
            finalAmount = originalAmount * 0.9; 
            finalAmount = Math.round(finalAmount * 100.0) / 100.0;
            bill.setTotalAmount(finalAmount);
        }

        try {
            // Attempt to add the bill record to the database
            boolean added = billdb.AddBill(bill);
            if (added) {
                try {
                    // Once paid, update the table status to free/available
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