package handlers;

import client.GuestReservationUI;
import javafx.application.Platform;

/**
 * Handles the response from the server for UPDATE_RESERVATION requests.
 * Updates the UI with a success/failure message.
 */
public class UpdateReservationHandler implements ResponseHandler {

    private final GuestReservationUI ui;

    public UpdateReservationHandler(GuestReservationUI ui) {
        this.ui = ui;
    }

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