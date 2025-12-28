package Entities;

import enums.UserRole;

public class RestaurantManager extends User{
	private String name;
	private String userName;
	
	public RestaurantManager(int userID, String name, String email, String phone, String userName) {
		super(userID, email, phone, UserRole.MANAGER);
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
