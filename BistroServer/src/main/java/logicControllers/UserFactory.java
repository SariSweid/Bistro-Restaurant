package logicControllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import Entities.Guest;
import Entities.Manager;
import Entities.Subscriber;
import Entities.Supervisor;
import Entities.User;
import enums.UserRole;

/**
 * UserFactory Class uses factory design pattern in order to create users
 */
public class UserFactory {
	
	/**
	 * private constructor in order to prevent instantiation
	 */
	private UserFactory(){
		
	}
	
	/**
	 * creates user object based on the result query from db and based on the role
	 * @param rs holds the result of the query of getting a user from db
	 * @return the user object depends on the role
	 * @throws SQLException
	 */
	public static User createUser(ResultSet rs) throws SQLException {
		int userID = rs.getInt("UserId");
		String name = rs.getString("Name");
		String phone = rs.getString("Phone");
		String email = rs.getString("Email");
		String userName = rs.getString("UserName");
		int membershipCode = rs.getInt("MemberShipCode");
		UserRole role = UserRole.valueOf(rs.getString("Role"));
		
		return switch (role) {
				case GUEST -> new Guest(userID, email, phone);
				case SUBSCRIBER -> new Subscriber(userID, name, email, phone, userName, membershipCode);
				case SUPERVISOR -> new Supervisor(userID, name, email, phone, userName, membershipCode);
				case MANAGER -> new Manager(userID, name, email, phone, userName, membershipCode);
		};
	}
}
