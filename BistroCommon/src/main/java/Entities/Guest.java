package Entities;

import java.io.Serializable;

import enums.UserRole;

public class Guest extends User implements Serializable {
	
	public Guest(int userID, String email, String phone) {
		super(userID, email, phone, UserRole.GUEST);
	}
}
