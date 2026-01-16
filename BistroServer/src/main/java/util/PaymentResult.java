package util;

import Entities.Bill;

/**
 * A utility class used to encapsulate the result of a payment processing operation.
 * It carries information regarding the success status, a descriptive message, 
 * and the resulting Bill entity if the transaction was processed.
 */
public class PaymentResult {
    
    /** Indicates whether the payment transaction was successful. */
    private final boolean success;
    
    /** A descriptive message explaining the outcome of the payment attempt. */
    private final String message;
    
    /** The bill associated with this payment result, containing final costs and details. */
    private final Bill bill;

    /**
     * Constructs a new PaymentResult with the specified status, message, and bill.
     *
     * @param success true if the payment was completed successfully, false otherwise.
     * @param message a String containing details about the success or failure.
     * @param bill the Bill entity related to this payment attempt.
     */
    public PaymentResult(boolean success, String message, Bill bill) {
        this.success = success;
        this.message = message;
        this.bill = bill;
    }

    /**
     * Checks if the payment was successful.
     * @return true if success is confirmed, false otherwise.
     */
    public boolean isSuccess() { 
        return success; 
    }

    /**
     * Retrieves the feedback message related to the payment process.
     * @return the status message as a String.
     */
    public String getMessage() { 
        return message; 
    }

    /**
     * Retrieves the bill entity associated with this result.
     * @return the Bill object containing transaction data.
     */
    public Bill getBill() { 
        return bill; 
    }
}