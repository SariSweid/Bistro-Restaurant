package messages;

import java.io.Serializable;

/**
 * Represents a request message to authenticate a user into the system.
 * This request carries the primary credentials required for a login attempt,
 * verifying the user's identity based on their ID and membership code.
 */
public class LoginRequest implements Serializable {
    
    /**
     * The serial version UID for ensuring serialization compatibility.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier (ID) of the user attempting to log in.
     */
    private final int userID;

    /**
     * The secret or unique membership code used to verify the user's authenticity.
     */
    private final int membershipCode;

    /**
     * Constructs a new LoginRequest with the specified user credentials.
     *
     * @param userID         the unique ID of the user
     * @param membershipCode the secret membership code for authentication
     */
    public LoginRequest(int userID, int membershipCode) {
        this.userID = userID;
        this.membershipCode = membershipCode;
    }

    /**
     * Returns the user ID associated with this login attempt.
     *
     * @return the user ID
     */
    public int getUserID() { 
        return userID; 
    }

    /**
     * Returns the membership code associated with this login attempt.
     *
     * @return the membership code
     */
    public int getMembershipCode() { 
        return membershipCode; 
    }
}