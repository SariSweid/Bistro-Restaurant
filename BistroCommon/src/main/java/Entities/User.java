package Entities;

import java.io.Serializable;

import enums.UserRole;

/**
 * User the base class for Subscriber, Guest, RestaurantSupervisor and RestaurantManager
 */
public abstract class User implements Serializable {
	
	//enum that represent each user role in the system, used to define when logging in to the system which screen each user will go
	/**
	 * 
	 */
	private int userID;
	private String email;
	private String phone;
	private UserRole role;
	
	/**
	 * constructor for new user
	 * @param userID
	 * @param email
	 * @param phone
	 * @param role
	 */
	protected User(int userID, String email, String phone, UserRole role) {
		this.userID = userID;
		this.email = email;
		this.phone = phone;
		this.role = role;
	}
	
	//getters
	
	public int getUserId() {
	    return this.userID;
	}
	
	public String getEmail() {
		return this.email;
	}

	public String getPhone() {
		return this.phone;
	}

	public UserRole getRole() {
		return this.role;
	}
	
	//setters
	
	public void setEmail(String newEmail) {
		this.email = newEmail;
	}
	
	public void setPhone(String newPhone) {
		this.phone = newPhone;
	}
}