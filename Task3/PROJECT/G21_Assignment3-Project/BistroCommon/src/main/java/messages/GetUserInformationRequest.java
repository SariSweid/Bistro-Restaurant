package messages;

import java.io.Serializable;

/**
 * Represents a request message to retrieve detailed information about a specific user.
 * This request uses the unique user ID to fetch profile data, contact details, 
 * or other account-related information from the system.
 */
@SuppressWarnings("serial")
public class GetUserInformationRequest implements Serializable {

    /**
     * The unique identifier (ID) of the user whose information is being requested.
     */
	private final int userID;

    /**
     * Constructs a new GetUserInformationRequest with the specified user ID.
     *
     * @param userID the unique ID of the user
     */
    public GetUserInformationRequest(int userID) {
        this.userID = userID;
    }

    /**
     * Returns the user ID associated with this request.
     *
     * @return the unique identifier of the user
     */
    public int getUserID() {
        return userID;
    }
}