package Entities;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a bill for a reservation.
 * Contains information about the bill ID, associated reservation ID,
 * total amount, issue date and time, and payment status.
 */
@SuppressWarnings("serial")
public class Bill implements Serializable {

    private int billID;
    private int reservationID;
    private double totalAmount;
    private LocalDateTime issuedAt;
    private boolean paid;

    /**
     * Constructor for creating a new bill.
     * Sets the issue time to the current time and marks the bill as unpaid.
     *
     * @param billID        the unique identifier for the bill
     * @param reservationID the ID of the associated reservation
     * @param totalAmount   the total amount of the bill
     */
    public Bill(int billID, int reservationID, double totalAmount) {
        this.billID = billID;
        this.reservationID = reservationID;
        this.totalAmount = totalAmount;
        this.issuedAt = LocalDateTime.now();
        this.paid = false;
    }

    /**
     * Constructor for creating a bill from database records.
     *
     * @param billID        the unique identifier for the bill
     * @param reservationID the ID of the associated reservation
     * @param totalAmount   the total amount of the bill
     * @param issuedAt      the date and time when the bill was issued
     * @param paid          the payment status of the bill
     */
    public Bill(int billID, int reservationID, double totalAmount, LocalDateTime issuedAt, boolean paid) {
        this.billID = billID;
        this.reservationID = reservationID;
        this.totalAmount = totalAmount;
        this.issuedAt = issuedAt;
        this.paid = paid;
    }

    /**
     * Returns the unique identifier of the bill.
     *
     * @return the bill ID
     */
    public int getBillID() {
        return this.billID;
    }

    /**
     * Sets the unique identifier of the bill.
     *
     * @param billID the new bill ID
     */
    public void setBillID(int billID) {
        this.billID = billID;
    }

    /**
     * Returns the ID of the associated reservation.
     *
     * @return the reservation ID
     */
    public int getReservationID() {
        return this.reservationID;
    }

    /**
     * Returns the total amount of the bill.
     *
     * @return the total amount
     */
    public double getTotalAmount() {
        return this.totalAmount;
    }

    /**
     * Updates the total amount of the bill.
     *
     * @param totalAmount the new total amount
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Returns the date and time when the bill was issued.
     *
     * @return the issue date and time
     */
    public LocalDateTime getIssuedAt() {
        return this.issuedAt;
    }

    /**
     * Returns whether the bill has been paid.
     *
     * @return true if the bill is paid, false otherwise
     */
    public boolean isPaid() {
        return this.paid;
    }

    /**
     * Marks the bill as paid.
     */
    public void markAsPaid() {
        this.paid = true;
    }
}
