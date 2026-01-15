package Entities;

import java.io.Serializable;

import enums.UserRole;
/**
 * RestaurantManager represents the restaurant manager
 */
@SuppressWarnings("serial")
public class Manager extends User implements Serializable {
	private String name;
	private String userName;
	private int membershipCode;
	
	/**
	 * constructor for new Restaurant Manager
	 * @param userID
	 * @param name
	 * @param email
	 * @param phone
	 * @param userName
	 * @param membershipCode
	 */
	public Manager(int userID, String name, String email, String phone, String userName, int membershipCode) {
		super(userID, email, phone, UserRole.MANAGER);
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
	
	public void setUserName(String userName) {
		this.userName = userName;
		
	}
}
