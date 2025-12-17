package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Reservation implements Serializable {

	//enum that represent the current status of the reservation use after the prototype
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
	private Date reservationDate; //future date the customer reserved a place
	private Date reservationPlacedDate; //when the reservation was palced in the site
	//private LocalDate reservationDate; //use after the prototype
	//private LocalTime reservationTime; //use after the prototype
	//private LocalDate reservationPlacedDate; //use after the prototype
	//private LocalTime reservationPlacedTime; //use after the prototype
	private int numOfGuests;
	private int confirmationCode;
	private Status status; //use after the prototype
	private final int customerID;
	//private Integer tableID; //use after the prototype
	//private Integer billID; //Integer and not int to initialize with null until bill is generated //use after the prototype
	
	//Constructor for creating a new reservation - use after prototype
	public Reservation(int reservationID, Date reservationDate, int numOfGuests, int confirmationCode, Status status, int customerID) {
		this.reservationID = reservationID;
		this.reservationDate = reservationDate;
		this.reservationPlacedDate = new Date();
		this.numOfGuests = numOfGuests;
		this.confirmationCode = confirmationCode;
		this.status = status;
		this.customerID = customerID;
		//this.tableID = null;
		//this.billID = null;
	}
	
	//constructor for getting a reservation from the db
	public Reservation(int reservationID, Date reservationDate, Date reservationPlacedDate, int numOfGuests, int confirmationCode, int customerID) {
		this.reservationID = reservationID;
		this.reservationDate = reservationDate;
		this.reservationPlacedDate = reservationPlacedDate;
		this.numOfGuests = numOfGuests;
		this.confirmationCode = confirmationCode;
		this.customerID = customerID;
	}
	
	//getters
	
	public int getReservationID() {
		return this.reservationID;
	}
	
	public Date getReservationDate() {
		return this.reservationDate;
	}
	
	
	public int getNumOfGuests() {
		return this.numOfGuests;
	}
	
	public int getConfirmationCode() {
		return this.confirmationCode;
	}

	public int getCustomerID() {
		return customerID;
	}

	public Status getStatus() {
		return this.status;
	}
	
	public int getSubscriberId() {
	    return this.customerID;
	}

	public Date getOrderDate() {
	    return this.reservationPlacedDate;
	}
	
	
	//getters use after the prototype
	/*public Integer getTableID() {
		return this.tableID;
	}
	
	public Integer getBillID() {
		return this.billID;
	}*/
	
	//setters
	
	public void setDate(Date newReservationDate) {
		this.reservationDate = newReservationDate;
	}
	
	//setter use after the prototype
	/*public void setTime(LocalTime newTime) {
		this.time = newTime;
	}*/
	
	public void setNumOfGuests(int newNumOfGuests) {
		this.numOfGuests = newNumOfGuests;
	}
	
	public void setConfirmationCode(int newConfirmationCode) {
		this.confirmationCode = newConfirmationCode;
	}

	
	//setters use after the prototype
	/*public void setStatus(Status newStatus) {
		this.status = newStatus;
	}
	
	public void setTableID(Integer newTableID) {
		this.tableID = newTableID;
	}
	
	public void setBillID(Integer newBillID) {
		this.billID = newBillID;
	}
	
	//status methods use after the prototype
	
	public boolean isReservationActive() {
		return (this.status == Status.CONFIRMED) || (this.status == Status.PENDING);
	}
	
	public boolean isReservationCancelled() {
		return this.status == Status.CANCELLED;
	}*/
}
