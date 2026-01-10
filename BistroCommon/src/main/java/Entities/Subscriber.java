package Entities;

import java.io.Serializable;

import enums.UserRole;

/**
 * Subscriber class represents a subscriber in the restaurant
 */
public class Subscriber extends User implements Serializable {
	private String name;
	private String userName;
	private int membershipCode;
	
	/**
	 * constructor for a new subscriber
	 * @param userID
	 * @param name
	 * @param email
	 * @param phone
	 * @param userName
	 * @param membershipCode
	 */
	public Subscriber(int userID, String name, String email, String phone, String userName, int membershipCode) {
		super(userID, email, phone, UserRole.SUBSCRIBER);
		this.name = name;
		this.userName = userName;
		this.membershipCode = membershipCode;
	}
	
	//getters
	
	public String getName() {
		return this.name;
	}
	
	
	public String getUserName() {
		return this.userName;
	}
	
	public int getMembershipCode() {
		return this.membershipCode;
	}
	
	//setters
	
	public void setName(String newName) {
		this.name = newName;
	}
}
