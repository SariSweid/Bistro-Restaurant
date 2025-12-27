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
	 * 
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
	
	/**
	 * 
	 * @return
	 */
	public int getuserId() {
	    return this.userID;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPhone() {
		return this.phone;
	}
	
	/**
	 * 
	 * @return
	 */
	public UserRole getRole() {
		return this.role;
	}
	
	public void setEmail(String newEmail) {
		this.email = newEmail;
	}
	
	public void setPhone(String newPhone) {
		this.phone = newPhone;
	}
}