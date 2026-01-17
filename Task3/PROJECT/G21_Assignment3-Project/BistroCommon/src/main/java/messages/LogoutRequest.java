package messages;

import java.io.Serializable;

/**
 * Represents a request message to log out a user from the system.
 * This request signals the server to terminate the current active session, 
 * clear session data, and update the user's online status if necessary.
 */
@SuppressWarnings("serial")
public class LogoutRequest implements Serializable {

    /**
     * Constructs a new LogoutRequest.
     */
    public LogoutRequest() {
        // Default constructor
    }
}