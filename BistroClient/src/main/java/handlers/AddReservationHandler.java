package handlers;

import Controllers.GuestMakeReservationController;
import client.GuestUpdateReservationUI;
import common.ServerResponse;
import javafx.application.Platform;

/**
 * Handles the response from the server for ADD_RESERVATION requests.
 * Updates the UI with a success/failure message.
 */
public class AddReservationHandler implements ResponseHandler {

	private final GuestMakeReservationController controller;

    public AddReservationHandler(GuestMakeReservationController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(Object data) {
        Platform.runLater(() -> {
            if (data instanceof ServerResponse res) {
                if (res.isSuccess()) {
                    controller.showConfirmation("Reservation added successfully!\nConfirmation code: " +
                            ((res.getData() instanceof Entities.Reservation r) ? r.getConfirmationCode() : "N/A"));
                } else {
                    controller.showError("Failed to add reservation: " + res.getMessage());
                }
            } else {
                controller.showError("Unexpected server response for Add Reservation.");
            }
        });
    }
}