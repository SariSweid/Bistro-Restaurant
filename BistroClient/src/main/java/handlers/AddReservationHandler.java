package handlers;

import client.GuestReservationUI;
import javafx.application.Platform;

/**
 * Handles the response from the server for ADD_RESERVATION requests.
 * Updates the UI with a success/failure message.
 */
public class AddReservationHandler implements ResponseHandler {

    private final GuestReservationUI ui;

    public AddReservationHandler(GuestReservationUI ui) {
        this.ui = ui;
    }

    @Override
    public void handle(Object data) {
    		Platform.runLater(() -> {
            if ("ADD_OK".equals(data)) {
                ui.showMessage("Reservation added successfully!");
            } else if ("ADD_FAIL".equals(data)) {
                ui.showMessage("Failed to add reservation.");
            } else {
                ui.showMessage("Unexpected server response for Add Reservation.");
            }
        });
    }
}