package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DB.DBController;
import Entities.*;
import enums.ReservationStatus;
import logicControllers.UserFactory;

/**
 * Data Access Object for Users (Subscriber, Guest, Manager, Supervisor)
 */
public class UserDAO extends DBController {
	
	
    /**
     * Inserts a new user into the database.
     *
     * @param u the user to insert
     * @return true if the insertion succeeded, false otherwise
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
                pst.setNull(6, java.sql.Types.INTEGER);              // membershipCode
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
     * Inserts a new guest into the database.
     *
     * @param g the guest to insert
     * @return guest id if the insertion succeeded, -1 otherwise
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
	        return keys.getInt(1); // ← auto-generated UserId

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
	
	
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


	private int generateRandomUserId2() {
	    Random random = new Random();
	    int id;
	    do {
	        id = 100000 + random.nextInt(900000); 
	    } while (userExists(id));
	    return id;
	}

 
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

	
	

	// get Guest by phone number
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

	// get Guest by email
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
     * Updates an existing user in the database.
     *
     * @param u the user object containing updated data
     * @return true if the update succeeded, false otherwise
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
     * Reads all users from the database.
     *
     * @return list of users
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
     * Retrieves a specific user by their ID.
     *
     * @param userId the user ID
     * @return the User if found, or null if not found
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
    
	
    // ---- ADDED - get User code----
    // Fetch subscriber by membership code
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
