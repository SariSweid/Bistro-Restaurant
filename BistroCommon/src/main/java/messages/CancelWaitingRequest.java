package messages;

import java.io.Serializable;

/**
 * Represents a request to cancel a waiting list entry.
 * 
 * Contains the confirmation code of the entry and the user ID associated
 * with the entry (null if the entry belongs to a guest).
 */
@SuppressWarnings("serial")
public class CancelWaitingRequest implements Serializable {

    /** The confirmation code of the waiting list entry to cancel. */
    private int confirmationCode;

    /** The user ID associated with the entry, null if a guest. */
    private Integer userId;

    /**
     * Constructs a new cancel request for a waiting list entry.
     *
     * @param confirmationCode the confirmation code of the entry
     * @param userId the user ID, null if guest
     */
    public CancelWaitingRequest(int confirmationCode, Integer userId) {
        this.confirmationCode = confirmationCode;
        this.userId = userId;
    }

    /** Returns the confirmation code of the entry. */
    public int getConfirmationCode() { return confirmationCode; }

    /** Returns the user ID associated with the entry. */
    public Integer getUserId() { return userId; }
}
