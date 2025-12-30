package Entities;

import enums.UserRole;

public class Subscriber extends User{
	private String name;
	private String userName;
	private int membershipCode;
	
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
	
	public int getMembershipCode() {//
		return this.membershipCode;
	}
	
	//setters
	
	public void setName(String newName) {
		this.name = newName;
	}
}
