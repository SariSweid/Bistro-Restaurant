package Entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Reservation {

	//enum that represent the current status of the reservation
	public enum Status{
		PENDING,    //reservation waiting for confirmation
		CONFIRMED,  //reservation confirmed
		CANCELLED,  //reservation cancelled
		SEATED,	    //customer arrived and currently seated
		COMPLETED,  //customer left and payed for the reservation
		NOT_SHOWED, //customer late or not arrived 
		WAITLIST    //the table is currently occupied, the customer is in the waitlist
	}
	
	private int reservationID;
	private Date reservationDate; //when the reservation was made in the site
	private Date reservationMadeDate; //future date the customer reserved a place
	//private LocalDate date;
	//private LocalTime time;
	private int numOfGuests;
	private int confirmationCode;
	private Status status;
	private final int customerID;
	private Integer tableID;
	private Integer billID; //Integer and not int to initialize with null until bill is generated
	
	public Reservation(int reservationID, Date reservationDate, int numOfGuests, int confirmationCode, Status status, int customerID) {
		this.reservationID = reservationID;
		this.reservationDate = reservationDate;
		this.reservationMadeDate = new Date();
		this.numOfGuests = numOfGuests;
		this.confirmationCode = confirmationCode;
		this.status = status;
		this.customerID = customerID;
		this.tableID = null;
		this.billID = null;
	}
	
	//getters
	
	public int getReservationID() {
		return this.reservationID;
	}
	
	public Date getReservationDate() {
		return this.reservationDate;
	}
	
	public Date getReservationMadedate() {
		return this.reservationMadeDate;
	}
	
	/*public LocalTime getTime() {
		return this.time;
	}*/
	
	public int getNumOfGuests() {
		return this.numOfGuests;
	}
	
	public int getConfirmationCode() {
		return this.confirmationCode;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public int getCustomerID() {
		return this.customerID;
	}
	
	public Integer getTableID() {
		return this.tableID;
	}
	
	public Integer getBillID() {
		return this.billID;
	}
	
	//setters
	
	public void setDate(Date newReservationDate) {
		this.reservationDate = newReservationDate;
	}
	
	/*public void setTime(LocalTime newTime) {
		this.time = newTime;
	}*/
	
	public void setNumOfGuests(int newNumOfGuests) {
		this.numOfGuests = newNumOfGuests;
	}
	
	public void setConfirmationCode(int newConfirmationCode) {
		this.confirmationCode = newConfirmationCode;
	}
	
	public void setStatus(Status newStatus) {
		this.status = newStatus;
	}
	
	public void setTableID(Integer newTableID) {
		this.tableID = newTableID;
	}
	
	public void setBillID(Integer newBillID) {
		this.billID = newBillID;
	}
	
	//status methods
	
	public boolean isReservationActive() {
		return (this.status == Status.CONFIRMED) || (this.status == Status.PENDING);
	}
	
	public boolean isReservationCancelled() {
		return this.status == Status.CANCELLED;
	}
}
