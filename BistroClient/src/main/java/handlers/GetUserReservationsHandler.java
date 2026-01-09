package handlers;

import java.util.List;

import Controllers.BaseDisplayController;
import Controllers.CancelReservationController;
import Controllers.OrderController;
import Entities.Reservation;
import javafx.application.Platform;


public class GetUserReservationsHandler implements ResponseHandler {

    @SuppressWarnings("unchecked")
    @Override
    public void handle(Object data) {
    		if (!(data instanceof List<?> list)) return;

        System.out.println("[GetUserReservationsHandler] received " + list.size() + " reservations");

        BaseDisplayController controller = ClientHandler.getClient().getActiveDisplayController();
        if (controller == null) return;

        Platform.runLater(() -> {
            controller.showReservations((List<Reservation>) list);
        });
    }
}
