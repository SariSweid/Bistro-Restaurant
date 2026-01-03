package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import enums.ReservationStatus;

/**
 * Reservation class represents a reservation in the restaurant
 */
public class Reservation implements Serializable {
	
	private int reservationID;
	private final int customerID;
	private Integer tableID; //id of the table that was assigned to the reservation, Integer and not int to initialize with null until table is assigned 
	private Integer billID; //Integer and not int to initialize with null until bill is generated
	private int numOfGuests;
	private int confirmationCode;
	private LocalDate reservationDate; //future date the customer reserved a place
	private LocalTime reservationTime; //future time the customer reserved a place
	private LocalDate reservationPlacedDate; //the date the reservation was placed in the site
	private LocalTime reservationPlacedTime; ///the time the reservation was placed in the site
	private ReservationStatus status;
	
	/**
	 * Constructor for new Reservation
	 * @param reservationID
	 * @param customerID
	 * @param numOfGuests
	 * @param confirmationCode
	 * @param reservationDate
	 * @param reservationTime
	 * @param status
	 */
	public Reservation(int reservationID, int customerID, int numOfGuests, int confirmationCode, LocalDate reservationDate,
					   LocalTime reservationTime, ReservationStatus status) {
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
	}
	
	/**
	 * Constructor for Reservation from db
	 * @param reservationID
	 * @param customerID
	 * @param tableID
	 * @param billID
	 * @param numOfGuests
	 * @param confirmationCode
	 * @param reservationDate
	 * @param reservationTime
	 * @param reservationPlacedDate
	 * @param reservationPlacedTime
	 * @param status
	 */
	public Reservation(int reservationID, int customerID, Integer tableID, Integer billID, int numOfGuests,
					   int confirmationCode, LocalDate reservationDate, LocalTime reservationTime,
					   LocalDate reservationPlacedDate, LocalTime reservationPlacedTime, ReservationStatus status) {
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
		
	}
	
	//getters
	
	public int getReservationID() {
		return this.reservationID;
	}
	
	public int getCustomerId() {
	    return this.customerID;
	}
	
	public Integer getTableID() {
		return this.tableID;
	}
	
	public Integer getBillID() {
		return this.billID;
	}
	
	public int getNumOfGuests() {
		return this.numOfGuests;
	}
	
	public int getConfirmationCode() {
		return this.confirmationCode;
	}
	
	public LocalDate getReservationDate() {
		return this.reservationDate;
	}
	
	public LocalTime getReservationTime() {
		return this.reservationTime;
	}
	
	public LocalDate getReservationPlacedDate() {
		return this.reservationPlacedDate;
	}
	
	public LocalTime getReservationPlacedTime() {
		return this.reservationPlacedTime;
	}
	
	public int getSubscriberId() {
	    return this.customerID;
	}
	
	public LocalDate getOrderDate() {
	    return this.reservationPlacedDate;
	}

	public ReservationStatus getStatus() {
		return this.status;
	}
	
	//setters
	
	public void setTableID(Integer newTableID) {
		this.tableID = newTableID;
	}
	
	public void setBillID(Integer newBillID) {
		this.billID = newBillID;
	}
	
	public void setNumOfGuests(int newNumOfGuests) {
		this.numOfGuests = newNumOfGuests;
	}
	
	public void setConfirmationCode(int newConfirmationCode) {
		this.confirmationCode = newConfirmationCode;
	}
	
	public void setReservationDate(LocalDate newReservationDate) {
		this.reservationDate = newReservationDate;
	}
	
	public void setReservationTime(LocalTime newReservationTime) {
		this.reservationTime = newReservationTime;
	}
	
	public void setStatus(ReservationStatus newStatus) {
		this.status = newStatus;
	}
	
	//status methods
	
	/**
	 * isReservationActive returns if the reservation is still active or not
	 * @return True if the Reservation is Still Active, False else
	 */
	public boolean isReservationActive() {
		return (this.status == ReservationStatus.CONFIRMED) || (this.status == ReservationStatus.PENDING);
	}
	
	/**
	 * isReservationCancelled returns if the reservation is cancelled or not
	 * @return True if the Reservation is Cancelled, False else
	 */
	public boolean isReservationCancelled() {
		return this.status == ReservationStatus.CANCELLED;
	}
}
