package logicControllers;

import java.util.List;

import DB.DBController;
import Entities.User;

/**
 * Server-side logic controller for users.
 */
public class UserController {

	private final DBController db;
	
	// Constructor
	public UserController() {
        this.db = new DBController(); // implement DBController to connect to your DB
    }
	
	/**
     * Add a new user. Returns true on success.
     */
	public boolean addUser(User user) throws Exception {
		if (user == null) return false;
        // Validation
        	if (user.getUserId() <= 0) return false;

        try {
            return db.InsertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	/**
     * Get user information by ID
     */
	public User getUserInformation(int userID) {
        if (userID <= 0) return null;

        try {
            return db.getUser(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
     * Return all users from DB.
     */
	public List<User> getAllUsers() {
        try {
            return db.readAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	 /**
     * Update a reservation's date and guest count.
     * Returns true if update succeeded, false otherwise.
     */
	public boolean updateUser(User user) {
        if (user == null) return false;
        if (user.getUserId() <= 0) return false;

        try {
            return db.UpdateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	
}
