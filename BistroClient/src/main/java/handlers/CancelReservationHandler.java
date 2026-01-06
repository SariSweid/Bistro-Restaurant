package handlers;

import Controllers.CancelReservationController;
import Controllers.GuestCancelReservationController;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class CancelReservationHandler implements ResponseHandler {

	@Override
    public void handle(Object data) {
        ServerResponse res = (ServerResponse) data;

        Platform.runLater(() -> {
            Object controller = ClientHandler.getClient().getActiveCancelController();
            if (controller == null) return;

            if (controller instanceof CancelReservationController subController) {
                // Subscriber logic
                if (res.isSuccess()) {
                    subController.refreshReservations();
                    util.SceneManager.showInfo("Reservation cancelled successfully.");
                } else {
                    util.SceneManager.showError("Failed to cancel reservation: " + res.getMessage());
                }

            } else if (controller instanceof GuestCancelReservationController guestController) {
                // Guest logic
                if (res.isSuccess()) {
                    guestController.onClose(); // close popup
                    guestController.showMessage("Reservation cancelled successfully.");
                } else {
                    guestController.showError("Failed to cancel reservation: " + res.getMessage());
                }

            } else {
                System.out.println("Unknown cancel controller type.");
            }
        });
    }
}