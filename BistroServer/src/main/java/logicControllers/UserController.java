package logicControllers;

import java.util.List;

import DB.DBController;
import Entities.Guest;
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
     * Return User from DB by code.
     */
	public User getUserByMembershipCode(int code) {
	    try {
	   		return db.getUserByMembershipCode(code);
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
	
	
	/**
	 * Find existing guest by email or phone.
	 * If not found, create a new guest user.
	 * Returns userID.
	 */
	public int getOrCreateGuest(String contact) {

	    if (contact == null || contact.isEmpty())
	        return -1;

	    try {
	        // Decide if email or phone
	        User existingUser;

	        if (contact.contains("@")) {
	            existingUser = db.getUserByEmail(contact);
	        } else {
	            existingUser = db.getUserByPhone(contact);
	        }

	        // If found – return ID
	        if (existingUser != null) {
	            return existingUser.getUserId();
	        }

	        // Else – create new guest
	        String email = contact.contains("@") ? contact : null;
	        String phone = contact.contains("@") ? null : contact;

	        User newGuest = new Guest(0, email, phone); 
	        // userID = 0 means "DB will generate it"

	        return db.insertGuestAndReturnId(newGuest);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

	

	
}
