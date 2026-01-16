package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DB.DBController;
import Entities.*;
import logicControllers.UserFactory;

/**
 * Data Access Object (DAO) for managing all user types in the system, including 
 * Subscriber, Guest, Manager, and Supervisor.
 * This class handles polymorphic database operations and integrates with UserFactory
 * to instantiate the correct User subclasses based on their roles.
 */
public class UserDAO extends DBController {
	
    /**
     * Inserts a new user into the database. 
     * The method determines the user type through polymorphic checks (instanceof)
     * to populate the correct columns, such as membership code for subscribers.
     *
     * @param u the User entity to be inserted.
     * @return true if the insertion was successful, false otherwise.
     */
    public Boolean InsertUser(User u) {
        Connection con = getConnection();

        try (PreparedStatement pst = con.prepareStatement(
                "INSERT INTO `user`  (UserId, Name, Phone, Email, UserName, MemberShipCode, Role)  VALUES (?, ?, ?, ?, ?, ?, ?)"
        )) {

            pst.setInt(1, u.getUserId());

            if (u instanceof Subscriber s) {
                pst.setString(2, s.getName());
                pst.setString(3, s.getPhone());
                pst.setString(4, s.getEmail());
                pst.setString(5, s.getUserName());
                pst.setInt(6, s.getMembershipCode());
                pst.setString(7, s.getRole().name());
            } 
            else if (u instanceof Guest g) {
                pst.setString(2, null);          // name
                pst.setString(3, g.getPhone());  // phone
                pst.setString(4, g.getEmail());  // email
                pst.setString(5, null);          // username
                pst.setNull(6, java.sql.Types.INTEGER);
                pst.setString(7, g.getRole().name()); // GUEST
            } 
            else if (u instanceof Manager m) {
                pst.setString(2, m.getName());
                pst.setString(3, m.getPhone());
                pst.setString(4, m.getEmail());
                pst.setString(5, m.getUserName());
                pst.setInt(6, 0);
                pst.setString(7, m.getRole().name());
            } 
            else if (u instanceof Supervisor s) {
                pst.setString(2, s.getName());
                pst.setString(3, s.getPhone());
                pst.setString(4, s.getEmail());
                pst.setString(5, s.getUserName());
                pst.setInt(6, 0);
                pst.setString(7, s.getRole().name());
            }

            int update_status = pst.executeUpdate();
            return update_status > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
	/**
     * Registers a new guest and returns their database-generated unique ID.
     *
     * @param g the guest user to insert.
     * @return the auto-generated integer ID if successful, -1 otherwise.
     */
	public int insertGuestAndReturnId(User g) {

	    try (Connection con = getConnection();
	         PreparedStatement pst =
	             con.prepareStatement(
	                 "INSERT INTO `user` (Phone, Email, Role) VALUES (?, ?, ?)",
	                 PreparedStatement.RETURN_GENERATED_KEYS)) {

	        pst.setString(1, g.getPhone());
	        pst.setString(2, g.getEmail());
	        pst.setString(3, g.getRole().name());

	        pst.executeUpdate();

	        ResultSet keys = pst.getGeneratedKeys();
	        keys.next();
	        return keys.getInt(1); // Auto-generated UserId

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
	
    /**
     * Alternative guest insertion that uses a custom random ID generator.
     * Useful when the business logic requires IDs within a specific numeric range.
     *
     * @param g the guest user to insert.
     * @return the manually generated random ID if successful, -1 otherwise.
     */
	public int insertGuestAndReturnId2(User g) {
	    int newId = generateRandomUserId2(); 
	    try (Connection con = getConnection();
	         PreparedStatement pst =
	             con.prepareStatement(
	                 "INSERT INTO `user` (UserId, Phone, Email, Role) VALUES (?, ?, ?, ?)")) {

	        pst.setInt(1, newId); 
	        pst.setString(2, g.getPhone());
	        pst.setString(3, g.getEmail());
	        pst.setString(4, g.getRole().name());

	        int rows = pst.executeUpdate();
	        if (rows > 0) {
	            return newId; 
	        } else {
	            return -1; 
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

    /**
     * Generates a unique 6-digit user ID and verifies it doesn't already exist.
     *
     * @return a unique 6-digit integer.
     */
	private int generateRandomUserId2() {
	    Random random = new Random();
	    int id;
	    do {
	        id = 100000 + random.nextInt(900000); 
	    } while (userExists(id));
	    return id;
	}

    /**
     * Checks if a specific User ID exists in the database.
     * * @param userId the ID to check.
     * @return true if the user exists, false otherwise.
     */
	private boolean userExists(int userId) {
	    try (Connection con = getConnection();
	         PreparedStatement ps = con.prepareStatement("SELECT 1 FROM `user` WHERE UserId = ?")) {
	        ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();
	        return rs.next();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return true;
	}

    /**
     * Retrieves a user from the database based on their phone number.
     *
     * @param phone the phone number string.
     * @return a User object if found, null otherwise.
     */
	public User getUserByPhone(String phone) {
	    try (Connection con = getConnection();
	         PreparedStatement pst =
	             con.prepareStatement("SELECT * FROM `user` WHERE Phone = ?")) {

	        pst.setString(1, phone);
	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) return UserFactory.createUser(rs);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

    /**
     * Retrieves a user from the database based on their email address.
     * * @param email the email address to search for.
     * @return a User object if found, null otherwise.
     */
	public User getUserByEmail(String email) {
	    try (Connection con = getConnection();
	         PreparedStatement pst =
	             con.prepareStatement("SELECT * FROM `user` WHERE Email = ?")) {

	        pst.setString(1, email);
	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) return UserFactory.createUser(rs);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
    /**
     * Updates an existing user's profile information.
     * Supports updating names and usernames for roles that possess them.
     *
     * @param u the user object containing the updated fields.
     * @return true if at least one row was updated.
     */
	public Boolean UpdateUser(User u) {
		Connection con = getConnection();
		
		try (PreparedStatement pst = con.prepareStatement("UPDATE `user` SET Name = ?, Phone = ?, Email = ?, UserName = ?  WHERE UserId = ?")) {	
			
			pst.setString(1, null);
			pst.setString(2, u.getPhone());  
            pst.setString(3, u.getEmail());
            pst.setString(4, null);
	        pst.setInt(5, u.getUserId());
	            
            if (u instanceof Subscriber s) {
                pst.setString(1, s.getName());
                pst.setString(4, s.getUserName());
            }
            else if (u instanceof Manager m) {
                pst.setString(1, m.getName());
                pst.setString(4, m.getUserName());
            }
            else if (u instanceof Supervisor s) {
                pst.setString(1, s.getName());
                pst.setString(4, s.getUserName());
            }
            int rows = pst.executeUpdate();

            return rows > 0; 

        } catch (SQLException e) {
            System.err.println("SQL Exception during update: " + e.getMessage());
            e.printStackTrace();
            return false; 
        }			
	}
	
    /**
     * Retrieves all users registered in the system.
     *
     * @return a List of all User entities.
     */
	public List<User> readAllUsers() {

	    List<User> users = new ArrayList<>();
	    Connection con = getConnection();

	    try (PreparedStatement pst =
	            con.prepareStatement("SELECT * FROM `user`")) {

	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	            User u = UserFactory.createUser(rs); 
	            users.add(u);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return users;
	}
	
    /**
     * Retrieves a specific user by their unique ID.
     *
     * @param userId the ID to search for.
     * @return the User object if found, null otherwise.
     */
    public User getUser(int userId) {

        Connection con = getConnection();
        User u = null;

        try (PreparedStatement pst =
                con.prepareStatement("SELECT * FROM `user` WHERE UserId = ?")) {

            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                u = UserFactory.createUser(rs); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }
    
    /**
     * Fetches a subscriber specifically by their unique membership code.
     *
     * @param code the membership integer code.
     * @return the User (typically a Subscriber) if found, null otherwise.
     */
    public User getUserByMembershipCode(int code) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(
                 "SELECT * FROM `user` WHERE MemberShipCode = ?")) {
            pst.setInt(1, code);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return UserFactory.createUser(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}