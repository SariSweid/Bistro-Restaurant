package Entities;

import java.io.Serializable;
import enums.UserRole;

/**
 * Represents a restaurant supervisor user in the system.
 * Inherits from the User class and includes additional information
 * specific to supervisors, such as name, username, and membership code.
 */
@SuppressWarnings("serial")
public class Supervisor extends User implements Serializable {

    /**
     * Full name of the supervisor.
     */
    private String name;

    /**
     * Username used for login or identification.
     */
    private String userName;

    /**
     * Membership code assigned to the supervisor.
     */
    private int membershipCode;

    /**
     * Constructs a new Supervisor instance with the specified information.
     *
     * @param userID         the unique user ID
     * @param name           the supervisor's full name
     * @param email          the supervisor's email
     * @param phone          the supervisor's phone number
     * @param userName       the username of the supervisor
     * @param membershipCode the membership code of the supervisor
     */
    public Supervisor(int userID, String name, String email, String phone, String userName, int membershipCode) {
        super(userID, email, phone, UserRole.SUPERVISOR);
        this.name = name;
        this.userName = userName;
        this.membershipCode = membershipCode;
    }

    /**
     * Returns the full name of the supervisor.
     *
     * @return the supervisor's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets a new name for the supervisor.
     *
     * @param newName the new name to set
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Returns the username of the supervisor.
     *
     * @return the supervisor's username
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Sets a new username for the supervisor.
     *
     * @param userName the new username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the membership code of the supervisor.
     *
     * @return the membership code
     */
    public int getMembershipCode() {
        return this.membershipCode;
    }
}
