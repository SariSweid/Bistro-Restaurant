package logicControllers;

import DB.DBController;
import Entities.Bill;
import common.ServerResponse;

public class PaymentController {
	
	
	
    
    private final DBController db;
    
    // Constructor
    public PaymentController() {
        this.db = new DBController(); // assumed DBController has a method to save bills
    }
    
    // Method to add a payment
    public Boolean addPayment(Bill bill) {

        Bill existingBill = db.getBillById(bill.getBillID());
        System.out.println("BILL" + existingBill);

        if (existingBill != null && existingBill.isPaid()) {
            return false;
        }

        try {
            db.AddBill(bill);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
    
    
   
