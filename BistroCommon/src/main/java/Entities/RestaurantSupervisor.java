package Entities;

import enums.UserRole;

public class RestaurantSupervisor extends User{
	private String name;
	private String userName;
	
	public RestaurantSupervisor(int userID, String name, String email, String phone, String userName) {
		super(userID, email, phone, UserRole.SUPERVISOR);
		this.name = name;
		this.userName = userName;
	}
	
	//getters
	
	public String getName() {
		return this.name;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	//setters
	
	public void setName(String newName) {
		this.name = newName;
	}
}
