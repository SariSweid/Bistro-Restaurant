package common;

import java.io.Serializable;

/**
 * Represents a standard response from the server.
 * 
 * Contains information about whether an operation succeeded, 
 * optional data returned by the server, and an associated message.
 */
public class ServerResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** True if the operation succeeded, false otherwise. */
    private final boolean success;

    /** Optional data returned by the server, for example a User object. */
    private final Object data;

    /** A message describing the result of the operation. */
    private final String message;

    /**
     * Constructs a new ServerResponse.
     *
     * @param success true if the operation succeeded, false otherwise
     * @param data optional data returned by the server
     * @param message a message describing the result
     */
    public ServerResponse(boolean success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    /**
     * Returns whether the operation was successful.
     *
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the optional data returned by the server.
     *
     * @return the data object, or null if none
     */
    public Object getData() {
        return data;
    }

    /**
     * Returns the message describing the result of the operation.
     *
     * @return the server message
     */
    public String getMessage() {
        return message;
    }
}
