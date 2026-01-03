package Entities;

import java.io.Serializable;

import enums.UserRole;
/**
 * RestaurantManager represents the restaurant manager
 */
public class RestaurantManager extends User implements Serializable {
	private String name;
	private String userName;
	
	/**
	 * constructor for new Restaurant Manager
	 * @param userID
	 * @param name
	 * @param email
	 * @param phone
	 * @param userName
	 */
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
