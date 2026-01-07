package Entities;

import java.io.Serializable;

import enums.UserRole;

/**
 * RestaurantSupervisor represents a Restaurant Supervisor
 */
public class Supervisor extends User implements Serializable {
	private String name;
	private String userName;
	private int membershipCode;
	
	/**
	 * constructor for new Restaurant Supervisor
	 * @param userID
	 * @param name
	 * @param email
	 * @param phone
	 * @param userName
	 * @param membershipCode
	 */
	public Supervisor(int userID, String name, String email, String phone, String userName, int membershipCode) {
		super(userID, email, phone, UserRole.SUPERVISOR);
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
