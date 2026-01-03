package Entities;

import java.io.Serializable;
import java.time.LocalTime;

import enums.ExitReason;
/**
 * WaitingListEntry class represents an entry in the waiting list
 */
public class WaitingListEntry implements Serializable {
	private Integer userID; // null in case of guest
	private String contactInfo; // email/phone
	private int numOfGuests;
	private int confirmationCode;
	private LocalTime entryTime; // when the customer entered the waiting list
	private LocalTime exitTime; // when the customer left the waiting list
	private ExitReason exitReason; // why the customer left the waiting list
	
	/**
	 * constructor for a new entry in the waiting list
	 * @param userID
	 * @param contactInfo
	 * @param numOfGuests
	 * @param confirmationCode
	 */
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
	
	/**
	 * exit method sets the exit time and exit reason from waiting list	
	 * @param exitReason 
	 */
	public void exit(ExitReason exitReason) {
		this.exitReason = exitReason;
		this.exitTime = LocalTime.now();
	}
}
