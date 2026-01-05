package logicControllers;

import DB.DBController;
import Entities.Bill;

public class PaymentController {
    
    private final DBController db;
    
    // Constructor
    public PaymentController() {
        this.db = new DBController(); // assumed DBController has a method to save bills
    }
    
    // Method to add a payment
    public boolean addPayment(Bill bill) {
        try {
            db.AddBill(bill); 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
