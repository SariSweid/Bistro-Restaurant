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
        //if (user.getUserID() <= 0) return false;

        try {
            //return db.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
		return false; //toRemove
    }
	
	/**
     * Get user information by ID
     */
	public User getUserInformation(int userID) {
        if (userID <= 0) return null;

        try {
            //return db.getUser(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		return null; //toRemove
    }
	
	/**
     * Return all users from DB.
     */
	public List<User> getAllUsers() {
        try {
            //return db.readAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		return null; //toRemove
    }
	
	 /**
     * Update a reservation's date and guest count.
     * Returns true if update succeeded, false otherwise.
     */
	public boolean updateUser(User user) {
        if (user == null) return false;
        //if (user.getUserID() <= 0) return false;

        try {
           // return db.updateUser(u);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
		return false; //toRemove
    }
	
	
}
