package Entities;

import java.io.Serializable;
import enums.UserRole;

/**
 * Abstract base class representing a user in the restaurant system.
 * Serves as the parent class for {@link Subscriber}, Guest, RestaurantSupervisor,
 * and RestaurantManager.
 * Stores common user attributes such as user ID, email, phone, and role.
 */
@SuppressWarnings("serial")
public abstract class User implements Serializable {

    private int userID;
    private String email;
    private String phone;
    private UserRole role;

    /**
     * Constructs a new user with the specified details.
     *
     * @param userID the unique identifier of the user
     * @param email  the email address of the user
     * @param phone  the phone number of the user
     * @param role   the role of the user in the system
     */
    protected User(int userID, String email, String phone, UserRole role) {
        this.userID = userID;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return the user ID
     */
    public int getUserId() {
        return this.userID;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return the phone number
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * Returns the role of the user in the system.
     *
     * @return the user role
     */
    public UserRole getRole() {
        return this.role;
    }

    /**
     * Sets a new email address for the user.
     *
     * @param newEmail the new email address
     */
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    /**
     * Sets a new phone number for the user.
     *
     * @param newPhone the new phone number
     */
    public void setPhone(String newPhone) {
        this.phone = newPhone;
    }

    /**
     * Sets a new role for the user.
     *
     * @param role the new user role
     */
    public void setRole(UserRole role) {
        this.role = role;
    }
}
