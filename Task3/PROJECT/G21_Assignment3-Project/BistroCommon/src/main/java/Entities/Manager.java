package Entities;

import java.io.Serializable;
import enums.UserRole;

/**
 * Represents a restaurant manager user.
 * Extends the User class and includes additional fields
 * such as name, username, and membership code.
 */
@SuppressWarnings("serial")
public class Manager extends User implements Serializable {

    private String name;
    private String userName;
    private int membershipCode;

    /**
     * Constructs a new Manager with the specified details.
     * 
     * @param userID the unique identifier of the manager
     * @param name the full name of the manager
     * @param email the email address of the manager
     * @param phone the phone number of the manager
     * @param userName the username for login
     * @param membershipCode the membership code assigned to the manager
     */
    public Manager(int userID, String name, String email, String phone, String userName, int membershipCode) {
        super(userID, email, phone, UserRole.MANAGER);
        this.name = name;
        this.userName = userName;
        this.membershipCode = membershipCode;
    }

    /**
     * Returns the full name of the manager.
     * 
     * @return the manager's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the username of the manager.
     * 
     * @return the manager's username
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Returns the membership code of the manager.
     * 
     * @return the manager's membership code
     */
    public int getMembershipCode() {
        return this.membershipCode;
    }

    /**
     * Sets a new full name for the manager.
     * 
     * @param newName the new name to set
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Sets a new username for the manager.
     * 
     * @param userName the new username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
