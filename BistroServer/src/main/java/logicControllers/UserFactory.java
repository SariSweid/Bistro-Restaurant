package logicControllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import Entities.Guest;
import Entities.User;
import enums.UserRole;

/**
 * UserFactory Class uses factory design pattern in order to create users
 */
public class UserFactory {
	
	//private in order to prevent instantiation
	private UserFactory(){
		
	}
	
	public static User createUser(ResultSet rs) throws SQLException {
		int userID = rs.getInt("UserId");
		String name = rs.getString("Name");
		String phone = rs.getString("Phone");
		String email = rs.getString("Email");
		String userName = rs.getString("UserName");
		String membershipCode = rs.getString("MemberShipCode");
		UserRole role = UserRole.valueOf(rs.getString("Role"));
		
		return switch (role) {
				case GUEST -> new Guest(userID, email, phone);
				case SUBSCRIBER -> new Subscriber() //need to finish
		};
	}
}
