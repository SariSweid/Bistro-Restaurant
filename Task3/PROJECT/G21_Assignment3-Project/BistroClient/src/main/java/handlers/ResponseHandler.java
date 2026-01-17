package handlers;

/**
 * Represents a handler for processing server responses.
 * 
 * Implementations of this interface define how to handle the data
 * returned from the server, such as updating the UI or triggering
 * other application logic.
 */
public interface ResponseHandler {

    /**
     * Handles the server response data.
     *
     * @param data the response object from the server
     */
    void handle(Object data);
}
