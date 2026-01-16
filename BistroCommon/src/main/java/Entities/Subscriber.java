package Entities;

import java.io.Serializable;
import enums.UserRole;

/**
 * Represents a subscriber user in the restaurant system.
 * Extends {@link User} and adds subscriber-specific information
 * such as name, username, and membership code.
 */
@SuppressWarnings("serial")
public class Subscriber extends User implements Serializable {

    private String name;
    private String userName;
    private int membershipCode;

    /**
     * Constructs a new subscriber with the given details.
     *
     * @param userID         the unique ID of the user
     * @param name           the full name of the subscriber
     * @param email          the email address of the subscriber
     * @param phone          the phone number of the subscriber
     * @param userName       the username for login purposes
     * @param membershipCode the membership code of the subscriber
     */
    public Subscriber(int userID, String name, String email, String phone, String userName, int membershipCode) {
        super(userID, email, phone, UserRole.SUBSCRIBER);
        this.name = name;
        this.userName = userName;
        this.membershipCode = membershipCode;
    }

    /**
     * Returns the full name of the subscriber.
     *
     * @return the name of the subscriber
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the username of the subscriber.
     *
     * @return the username
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Returns the membership code of the subscriber.
     *
     * @return the membership code
     */
    public int getMembershipCode() {
        return this.membershipCode;
    }

    /**
     * Sets a new name for the subscriber.
     *
     * @param newName the new full name
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Sets a new username for the subscriber.
     *
     * @param userName the new username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
