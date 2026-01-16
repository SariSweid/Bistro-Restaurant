package handlers;

import client.GuestUpdateReservationUI;
import javafx.application.Platform;

/**
 * Handles server responses for update reservation requests.
 * 
 * This handler updates the GuestUpdateReservationUI with a message indicating
 * whether the reservation update was successful or failed. All UI updates
 * are performed on the JavaFX Application Thread.
 */
public class UpdateReservationHandler implements ResponseHandler {

    private final GuestUpdateReservationUI ui;

    /**
     * Creates a new UpdateReservationHandler.
     *
     * @param ui the UI component to update with messages
     */
    public UpdateReservationHandler(GuestUpdateReservationUI ui) {
        this.ui = ui;
    }

    /**
     * Processes the server response for an update reservation request.
     * 
     * Displays a success message if the server response is "UPDATE_OK",
     * a failure message if the response is "UPDATE_FAIL", or a generic
     * message for any unexpected responses.
     *
     * @param data the server response object
     */
    @Override
    public void handle(Object data) {
        Platform.runLater(() -> {
            if ("UPDATE_OK".equals(data)) {
                ui.showMessage("Reservation updated successfully!");
            } else if ("UPDATE_FAIL".equals(data)) {
                ui.showMessage("Failed to update reservation.");
            } else {
                ui.showMessage("Unexpected server response for Update Reservation.");
            }
        });
    }
}
