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
                    util.SceneManager.showInfo(res.getMessage());
                } else {
                    util.SceneManager.showError(res.getMessage());
                }

            } else if (controller instanceof GuestCancelReservationController guestController) {
                // Guest logic
                if (res.isSuccess()) {
                    guestController.onClose(); // close popup
                    guestController.showMessage(res.getMessage());
                } else {
                    guestController.showError(res.getMessage() + "\nThere is no reservation with that confirmatin code.");
                }

            } else {
                System.out.println("Unknown cancel controller type.");
            }
        });
    }
}