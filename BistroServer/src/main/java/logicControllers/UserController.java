package logicControllers;

import java.util.List;

import DAO.UserDAO;
import Entities.Guest;
import Entities.User;

/**
 * Server-side logic controller for users.
 * Uses UserDAO to handle database operations.
 */
public class UserController {

    private final UserDAO userDAO;
    
    // Constructor
    public UserController() {
        this.userDAO = new UserDAO(); // DAO handles DB operations
    }
    
    /**
     * Add a new user. Returns true on success.
     */
    public boolean addUser(User user) throws Exception {
        if (user == null) return false;
        if (user.getUserId() <= 0) return false;

        try {
            return userDAO.InsertUser(user);
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
            return userDAO.getUser(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Return User from DB by membership code.
     */
    public User getUserByMembershipCode(int code) {
        try {
            return userDAO.getUserByMembershipCode(code);
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
            return userDAO.readAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Update a user's information.
     * Returns true if update succeeded, false otherwise.
     */
    public boolean updateUser(User user) {
        if (user == null) return false;
        if (user.getUserId() <= 0) return false;

        try {
            return userDAO.UpdateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * getOrCreateGuest is not needed for now.
     */
}
