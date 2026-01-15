package handlers;

import java.util.List;

import Controllers.BaseDisplayController;
import Controllers.BaseReservationController;
import Entities.Reservation;
import common.ServerResponse;
import javafx.application.Platform;

/**
 * Handles server responses for adding a new reservation.
 * Updates the active reservation controller and refreshes the display if needed.
 */
public class AddReservationHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {

        BaseReservationController controller =
                ClientHandler.getClient().getActiveReservationController();

        if (controller == null) {
            System.out.println("No active reservation controller found");
            return;
        }

        if (!(data instanceof ServerResponse response)) {
            Platform.runLater(() ->
                    controller.showError("Unexpected server response for Add Reservation"));
            return;
        }

        Platform.runLater(() -> {
            if (response.isSuccess()) {

                Reservation reservation =
                        response.getData() instanceof Reservation r ? r : null;

                String confirmation =
                        reservation != null
                                ? String.valueOf(reservation.getConfirmationCode())
                                : "N/A";

                controller.showConfirmation(
                        "Reservation added successfully!\nConfirmation code: " + confirmation
                );

                BaseDisplayController displayController =
                        ClientHandler.getClient().getActiveDisplayController();

                if (displayController != null && reservation != null) {
                    displayController.showReservations(List.of(reservation));
                }

            } else {
                controller.showError("Failed to add reservation: " + response.getMessage());
            }
        });
    }
}
