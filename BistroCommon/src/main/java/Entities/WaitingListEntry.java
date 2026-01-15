package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import enums.ExitReason;
import enums.WaitingStatus;
/**
 * WaitingListEntry class represents an entry in the waiting list
 */
@SuppressWarnings("serial")
public class WaitingListEntry implements Serializable {
	private Integer userID; // null in case of guest
	private String Email;// email/phone
	private String phone;
	private int numOfGuests;
	private int confirmationCode;
	private LocalDate WaitDate;
	private LocalTime WaitTime;
	private ExitReason exitReason; // why the customer left the waiting list
	private WaitingStatus status;   // WAITING / NOTIFIED / SEATED
	
	
	public WaitingListEntry(Integer userID, String email, String phone, int numOfGuests, int confirmationCode,
			LocalDate waitDate, LocalTime waitTime, ExitReason exitReason, WaitingStatus status) {
		
		
		this.userID = userID;
		Email = email;
		this.phone = phone;
		this.numOfGuests = numOfGuests;
		this.confirmationCode = confirmationCode;
		WaitDate = waitDate;
		WaitTime = waitTime;
		this.exitReason = exitReason;
		this.status = status;
	}
	
	public WaitingListEntry(Integer userID, String email, String phone, int numOfGuests, int confirmationCode,
			LocalDate waitDate, LocalTime waitTime, ExitReason exitReason) {
		
		
		this.userID = userID;
		Email = email;
		this.phone = phone;
		this.numOfGuests = numOfGuests;
		this.confirmationCode = confirmationCode;
		WaitDate = waitDate;
		WaitTime = waitTime;
		this.exitReason = exitReason;
	}

	
	//getters
	
	public Integer getUserID() {
		return userID;
	}


	public void setUserID(Integer userID) {
		this.userID = userID;
	}


	public String getEmail() {
		return Email;
	}


	public void setEmail(String email) {
		Email = email;
	}

	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public LocalDate getWaitDate() {
		return WaitDate;
	}


	public void setWaitDate(LocalDate waitDate) {
		WaitDate = waitDate;
	}


	public LocalTime getWaitTime() {
		return WaitTime;
	}

	public void setWaitTime(LocalTime waitTime) {
		WaitTime = waitTime;
	}


	public void setNumOfGuests(int numOfGuests) {
		this.numOfGuests = numOfGuests;
	}


	public void setConfirmationCode(int confirmationCode) {
		this.confirmationCode = confirmationCode;
	}


	public void setExitReason(ExitReason exitReason) {
		this.exitReason = exitReason;
	}

	

	public int getNumOfGuests() {
		return this.numOfGuests;
	}
	
	public int getConfirmationCode() {
		return this.confirmationCode;
	}
	
	
	public ExitReason getExitReason() {
		return this.exitReason;
	}

	public void exit(ExitReason exitReason) {
		this.exitReason = exitReason;
	}


	public void setStatus(WaitingStatus status) {
	    this.status = status;
	}
	
	public WaitingStatus getStatus() {
		return status;
	}



}
