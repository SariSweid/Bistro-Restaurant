package messages;

import java.io.Serializable;

/**
 * Represents a request message to retrieve a complete list of all users from the system.
 * This request is typically used by administrative components to fetch every registered 
 * user account without specific filtering criteria.
 */
@SuppressWarnings("serial")
public class GetAllUsersRequest implements Serializable {

    /**
     * Constructs a new GetAllUsersRequest.
     */
    public GetAllUsersRequest() {
        // Default constructor
    }
}