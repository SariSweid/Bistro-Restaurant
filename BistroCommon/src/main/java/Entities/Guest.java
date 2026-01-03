package Entities;

import java.io.Serializable;

import enums.UserRole;
/**
 * Guest class represents a guest at the restaurant
 */
public class Guest extends User implements Serializable {
	
	/**
	 * constructor for new guest
	 * @param userID
	 * @param email
	 * @param phone
	 */
	public Guest(int userID, String email, String phone) {
		super(userID, email, phone, UserRole.GUEST);
	}
}
