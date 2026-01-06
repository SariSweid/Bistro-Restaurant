package util;

import Entities.Bill;

public class PaymentResult {
    private final boolean success;
    private final String message;
    private final Bill bill;

    public PaymentResult(boolean success, String message, Bill bill) {
        this.success = success;
        this.message = message;
        this.bill = bill;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Bill getBill() { return bill; }
}
