package Entities;

import java.io.Serializable;

public class Guest extends User implements Serializable {
	
	public Guest(int userID, String email, String phone) {
		super(userID, email, phone, User.Role.GUEST);
	}
}
