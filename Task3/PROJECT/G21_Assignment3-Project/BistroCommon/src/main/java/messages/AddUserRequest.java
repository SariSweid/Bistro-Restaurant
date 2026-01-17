package messages;

import java.io.Serializable;
import Entities.User;

/**
 * Represents a request to add a new user.
 * Encapsulates a User object that should be added.
 */
@SuppressWarnings("serial")
public class AddUserRequest implements Serializable {

    private User user;

    /**
     * Constructs a new AddUserRequest with the specified user.
     * 
     * @param user the User object to be added
     */
    public AddUserRequest(User user) {
        this.user = user;
    }

    /**
     * Returns the User object contained in this request.
     * 
     * @return the User to be added
     */
    public User getUser() {
        return user;
    }
}
