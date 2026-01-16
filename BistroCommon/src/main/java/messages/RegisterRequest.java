package messages;

import java.io.Serializable;
import enums.UserRole;

/**
 * Represents a registration request message sent within the system.
 * This class encapsulates all necessary details required to register a new user.
 * It implements Serializable to allow objects to be transmitted over a network.
 */
public class RegisterRequest implements Serializable {
    
    /** Serial version UID for ensuring serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /** The unique identifier for the user. */
    private final int userID;
    
    /** The full name of the user. */
    private final String name;
    
    /** The email address of the user. */
    private final String email;
    
    /** The phone number of the user. */
    private final String phone;
    
    /** The login username chosen by the user. */
    private final String username;
    
    /** The specific membership or subscription code. */
    private final int membershipCode;
    
    /** The defined role of the user within the system (e.g., ADMIN, CLIENT). */
    private final UserRole role;

    /**
     * Constructs a new RegisterRequest with the specified details.
     *
     * @param userID          the unique ID of the user
     * @param name            the full name of the user
     * @param email           the user's email address
     * @param phone           the user's contact phone number
     * @param username        the username for system authentication
     * @param membershipCode  the code representing the user's membership level
     * @param role            the UserRole assigned to the user
     */
    public RegisterRequest(int userID, String name, String email, String phone,
                           String username, int membershipCode, UserRole role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.membershipCode = membershipCode;
        this.role = role;
    }

    /**
     * Gets the user ID.
     * * @return the integer userID
     */
    public int getUserID() { return userID; }

    /**
     * Gets the user's full name.
     * * @return the name string
     */
    public String getName() { return name; }

    /**
     * Gets the user's email.
     * * @return the email string
     */
    public String getEmail() { return email; }

    /**
     * Gets the user's phone number.
     * * @return the phone number string
     */
    public String getPhone() { return phone; }

    /**
     * Gets the chosen username.
     * * @return the username string
     */
    public String getUsername() { return username; }

    /**
     * Gets the membership code.
     * * @return the integer membershipCode
     */
    public int getMembershipCode() { return membershipCode; }

    /**
     * Gets the role assigned to the user.
     * * @return the UserRole constant
     */
    public UserRole getRole() { return role; }
    
}