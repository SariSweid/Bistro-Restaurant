package handlers;

import Controllers.BaseDisplayController;
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
            BaseDisplayController controller = ClientHandler.getClient().getActiveDisplayController();
            if (controller == null) return;

            if (controller instanceof CancelReservationController subController) {
                if (res.isSuccess()) {
                    subController.refreshReservations();
                    SceneManager.showInfo(res.getMessage());
                } else {
                    SceneManager.showError(res.getMessage());
                }
            } else if (controller instanceof GuestCancelReservationController guestController) {
                if (res.isSuccess()) {
                    guestController.onClose();
                    guestController.showMessage(res.getMessage());
                } else {
                    guestController.showError(res.getMessage() + "\nThere is no reservation with that confirmation code.");
                }
            }
        });

    }
}