package handlers;

/**
 * Handles the server response for marking a reservation as notified.
 * 
 * This handler does not update the UI because the UI is already updated
 * when the notification alert is shown. It logs a confirmation message
 * to indicate that the server processed the request.
 */
public class MarkNotifiedHandler implements ResponseHandler {

    /**
     * Processes the server response for a "mark as notified" request.
     * 
     * @param data the response object from the server (ignored)
     */
    @Override
    public void handle(Object data) {
        System.out.println("Server confirmed: Reservation marked as notified.");
    }
}
