package messages;

import java.io.Serializable;
import Entities.User;

/**
 * Represents a request message to update an existing user's information in the system.
 * Contains the User entity object with the updated details.
 */
@SuppressWarnings("serial")
public class UpdateUserRequest implements Serializable {

    /**
     * The User entity containing updated information.
     */
    private final User user;

    /**
     * Constructs a new UpdateUserRequest with the specified user.
     *
     * @param user the User object containing the updated details
     */
    public UpdateUserRequest(User user) {
        this.user = user;
    }

    /**
     * Returns the user associated with this update request.
     *
     * @return the User entity object
     */
    public User getUser() {
        return user;
    }
}