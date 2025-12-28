package Entities;

import java.time.LocalTime;

import enums.ExitReason;

public class WaitingListEntry {
	private Integer userID; // null in case of guest
	private String contactInfo; // email/phone
	private int numOfGuests;
	private int confirmationCode;
	private LocalTime entryTime; // when the customer entered the waiting list
	private LocalTime exitTime; // when the customer left the waiting list
	private ExitReason exitReason; // why the customer left the waiting list
	
	public WaitingListEntry(int userID, String contactInfo, int numOfGuests, int confirmationCode) {
		this.userID = userID;
		this.contactInfo = contactInfo;
		this.numOfGuests = numOfGuests;
		this.confirmationCode = confirmationCode;
		this.entryTime = LocalTime.now();
	}
	
	//getters
	
	public int getUserId() {
	    return this.userID;
	}
	
	public String getContactInfo() {
		return this.contactInfo;
	}
	
	public int getNumOfGuests() {
		return this.numOfGuests;
	}
	
	public int getConfirmationCode() {
		return this.confirmationCode;
	}
	
	public LocalTime getEntryTime() {
		return this.entryTime;
	}
	
	public LocalTime getExitTime() {
		return this.exitTime;
	}
	
	public ExitReason getExitReason() {
		return this.exitReason;
	}
	
	//לבדוק אם צריך setters
	
	/**
	 * exit method sets the exit time and exit reason from waiting list	
	 * @param exitReason 
	 */
	public void exit(ExitReason exitReason) {
		this.exitReason = exitReason;
		this.exitTime = LocalTime.now();
	}
}
