package handlers;

import java.util.List;

import Controllers.CancelReservationController;
import Entities.Reservation;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class GetUserReservationsHandler implements ResponseHandler {

	@Override
    public void handle(Object data) {
        ServerResponse res = (ServerResponse) data;

        Platform.runLater(() -> {
            CancelReservationController controller = 
                (CancelReservationController) ClientHandler.getClient().getActiveCancelController();

            if (controller == null) return;

            if (res.isSuccess() && res.getData() instanceof List<?> list) {
                //noinspection unchecked
                controller.loadReservations((List<Reservation>) list);
            } else {
                SceneManager.showError("Failed to load your reservations: " + res.getMessage());
            }
        });
    }
}