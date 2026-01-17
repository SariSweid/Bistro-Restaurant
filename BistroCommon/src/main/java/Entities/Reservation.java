package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import enums.ReservationStatus;

/**
 * Represents a reservation in the restaurant.
 * Stores information about the customer, table, bill, 
 * number of guests, reservation date and time, status, and notifications.
 */
@SuppressWarnings("serial")
public class Reservation implements Serializable {

    private int reservationID;
    private final Integer customerID;
    private Integer tableID;
    private Integer billID;
    private int numOfGuests;
    private int confirmationCode;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private LocalDate reservationPlacedDate;
    private LocalTime reservationPlacedTime;
    private ReservationStatus status;
    private LocalTime expectedDepartureTime;
    private LocalTime actualArrivalTime;
    private boolean isNotified;
    private boolean PaymentReminderSent;

    /**
     * Constructs a new Reservation for a customer.
     * Automatically sets the reservation placed date and time to now.
     * Table and bill IDs are initialized as null.
     * 
     * @param reservationID the unique ID of the reservation
     * @param customerID the ID of the customer making the reservation
     * @param numOfGuests the number of guests for the reservation
     * @param confirmationCode the confirmation code for the reservation
     * @param reservationDate the date of the reservation
     * @param reservationTime the time of the reservation
     * @param status the current status of the reservation
     * @param isNotified whether the customer has been notified
     */
    public Reservation(int reservationID, int customerID, int numOfGuests, int confirmationCode,
                       LocalDate reservationDate, LocalTime reservationTime,
                       ReservationStatus status, boolean isNotified) {
        this.reservationID = reservationID;
        this.customerID = customerID;
        this.numOfGuests = numOfGuests;
        this.confirmationCode = confirmationCode;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.reservationPlacedDate = LocalDate.now();
        this.reservationPlacedTime = LocalTime.now();
        this.status = status;
        this.tableID = null;
        this.billID = null;
        this.isNotified = isNotified;
    }

    /**
     * Constructs a Reservation object from the database.
     * All fields are initialized based on the stored values.
     * 
     * @param reservationID the unique ID of the reservation
     * @param customerID the ID of the customer making the reservation
     * @param tableID the ID of the assigned table
     * @param billID the ID of the generated bill
     * @param numOfGuests the number of guests
     * @param confirmationCode the confirmation code
     * @param reservationDate the date of the reservation
     * @param reservationTime the time of the reservation
     * @param reservationPlacedDate the date the reservation was placed
     * @param reservationPlacedTime the time the reservation was placed
     * @param status the status of the reservation
     * @param isNotified whether the customer has been notified
     */
    public Reservation(int reservationID, int customerID, Integer tableID, Integer billID,
                       int numOfGuests, int confirmationCode, LocalDate reservationDate,
                       LocalTime reservationTime, LocalDate reservationPlacedDate,
                       LocalTime reservationPlacedTime, ReservationStatus status,
                       boolean isNotified) {
        this.reservationID = reservationID;
        this.customerID = customerID;
        this.tableID = tableID;
        this.billID = billID;
        this.numOfGuests = numOfGuests;
        this.confirmationCode = confirmationCode;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.reservationPlacedDate = reservationPlacedDate;
        this.reservationPlacedTime = reservationPlacedTime;
        this.status = status;
        this.isNotified = isNotified;
    }

    // Getters

    /**
     * Returns the customer ID of the reservation.
     * 
     * @return the customer ID
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     * Returns the reservation ID.
     * 
     * @return the reservation ID
     */
    public int getReservationID() {
        return this.reservationID;
    }

    /**
     * Returns the assigned table ID.
     * 
     * @return the table ID, or null if no table is assigned
     */
    public Integer getTableID() {
        return this.tableID;
    }

    /**
     * Returns the bill ID.
     * 
     * @return the bill ID, or null if no bill is generated
     */
    public Integer getBillID() {
        return this.billID;
    }

    /**
     * Returns the number of guests for the reservation.
     * 
     * @return number of guests
     */
    public int getNumOfGuests() {
        return this.numOfGuests;
    }

    /**
     * Returns the confirmation code of the reservation.
     * 
     * @return the confirmation code
     */
    public int getConfirmationCode() {
        return this.confirmationCode;
    }

    /**
     * Returns the reservation date.
     * 
     * @return the reservation date
     */
    public LocalDate getReservationDate() {
        return this.reservationDate;
    }

    /**
     * Returns the reservation time.
     * 
     * @return the reservation time
     */
    public LocalTime getReservationTime() {
        return this.reservationTime;
    }

    /**
     * Returns the date when the reservation was placed.
     * 
     * @return the reservation placed date
     */
    public LocalDate getReservationPlacedDate() {
        return this.reservationPlacedDate;
    }

    /**
     * Returns the time when the reservation was placed.
     * 
     * @return the reservation placed time
     */
    public LocalTime getReservationPlacedTime() {
        return this.reservationPlacedTime;
    }

    /**
     * Returns the current status of the reservation.
     * 
     * @return the reservation status
     */
    public ReservationStatus getStatus() {
        return this.status;
    }

    /**
     * Returns whether the customer has been notified.
     * 
     * @return true if notified, false otherwise
     */
    public boolean isNotified() {
        return this.isNotified;
    }

    // Setters

    /**
     * Sets the reservation ID.
     * 
     * @param reservationID the new reservation ID
     */
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    /**
     * Sets the table ID.
     * 
     * @param newTableID the table ID to assign
     */
    public void setTableID(Integer newTableID) {
        this.tableID = newTableID;
    }

    /**
     * Sets the bill ID.
     * 
     * @param newBillID the bill ID to assign
     */
    public void setBillID(Integer newBillID) {
        this.billID = newBillID;
    }

    /**
     * Sets the number of guests.
     * 
     * @param newNumOfGuests the number of guests
     */
    public void setNumOfGuests(int newNumOfGuests) {
        this.numOfGuests = newNumOfGuests;
    }

    /**
     * Sets the confirmation code.
     * 
     * @param newConfirmationCode the confirmation code
     */
    public void setConfirmationCode(int newConfirmationCode) {
        this.confirmationCode = newConfirmationCode;
    }

    /**
     * Sets the reservation date.
     * 
     * @param newReservationDate the reservation date
     */
    public void setReservationDate(LocalDate newReservationDate) {
        this.reservationDate = newReservationDate;
    }

    /**
     * Sets the reservation time.
     * 
     * @param newReservationTime the reservation time
     */
    public void setReservationTime(LocalTime newReservationTime) {
        this.reservationTime = newReservationTime;
    }

    /**
     * Sets the reservation status.
     * 
     * @param newStatus the new reservation status
     */
    public void setStatus(ReservationStatus newStatus) {
        this.status = newStatus;
    }

    // Status methods

    /**
     * Returns true if the reservation is active.
     * Active statuses are CONFIRMED, PENDING, or SEATED.
     * 
     * @return true if the reservation is active, false otherwise
     */
    public boolean isReservationActive() {
        return (this.status == ReservationStatus.CONFIRMED ||
                this.status == ReservationStatus.PENDING ||
                this.status == ReservationStatus.SEATED);
    }

    /**
     * Returns true if the reservation is cancelled.
     * 
     * @return true if the reservation is cancelled, false otherwise
     */
    public boolean isReservationCancelled() {
        return this.status == ReservationStatus.CANCELLED;
    }

    /**
     * Sets the expected departure time.
     * 
     * @param localTime the expected departure time
     */
    public void setExpectedDepartureTime(LocalTime localTime) {
        this.expectedDepartureTime = localTime;
    }

    /**
     * Returns the expected departure time.
     * 
     * @return the expected departure time
     */
    public LocalTime getExpectedDepartureTime() {
        return this.expectedDepartureTime;
    }

    /**
     * Sets the actual arrival time of the customer.
     * 
     * @param t the actual arrival time
     */
    public void setActualArrivalTime(LocalTime t) {
        this.actualArrivalTime = t;
    }

    /**
     * Returns the actual arrival time of the customer.
     * 
     * @return the actual arrival time
     */
    public LocalTime getActualArrivalTime() {
        return this.actualArrivalTime;
    }

    /**
     * Sets whether the customer has been notified.
     * 
     * @param notified true if notified, false otherwise
     */
    public void setNotified(boolean notified) {
        this.isNotified = notified;
    }

	public void setPaymentReminderSent(boolean b) {
		this.PaymentReminderSent = b;	
		System.out.println("[Reservation] setPaymentReminderSent called for ID " + reservationID + " -> " + b);
	}
	
	public boolean isPaymentReminderSent() {
		System.out.println("[Reservation] setPaymentReminderSent called for ID " + reservationID + " -> " + PaymentReminderSent);
		return PaymentReminderSent;
	}
	
}
