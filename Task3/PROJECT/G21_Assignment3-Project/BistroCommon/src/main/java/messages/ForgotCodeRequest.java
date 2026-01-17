package messages;

import java.io.Serializable;

/**
 * Represents a request message sent when a user has forgotten their access code.
 * Contains the unique user ID required to initiate the code recovery process.
 */
@SuppressWarnings("serial")
public class ForgotCodeRequest implements Serializable {

    /**
     * The unique identifier (ID) of the user who requested the code recovery.
     */
    private Integer userId;

    /**
     * Constructs a new ForgotCodeRequest with the specified user ID.
     *
     * @param userId the ID of the user requesting the code
     */
    public ForgotCodeRequest(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the user ID associated with this request.
     *
     * @return the Integer representing the user ID
     */
    public Integer getUserId() {
        return userId;
    }
}