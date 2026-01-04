package handlers;

import Controllers.CancelReservationController;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class CancelReservationHandler implements ResponseHandler {

	@Override
    public void handle(Object data) {
        ServerResponse res = (ServerResponse) data;

        Platform.runLater(() -> {
            CancelReservationController controller = 
                (CancelReservationController) ClientHandler.getClient().getActiveCancelController();

            if (controller == null) return;

            if (res.isSuccess()) {
                SceneManager.showInfo("Reservation cancelled successfully.");
                // Refresh table
                controller.refreshReservations();
            } else {
                SceneManager.showError("Failed to cancel reservation: " + res.getMessage());
            }
        });
    }
}