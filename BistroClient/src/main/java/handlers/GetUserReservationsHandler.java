package handlers;

import java.util.List;
import Controllers.OrderController;
import Controllers.CancelReservationController;
import Entities.Reservation;
import javafx.application.Platform;

public class GetUserReservationsHandler implements ResponseHandler {

    @SuppressWarnings("unchecked")
    @Override
    public void handle(Object data) {
        if (!(data instanceof List<?> list)) return;

        System.out.println("[GetUserReservationsHandler] received " + list.size() + " reservations");

        Object active = ClientHandler.getClient().getActiveCancelController(); // may be order or cancel
        if (active == null) {
            active = ClientHandler.getClient().getOrderController(); // try order screen
        }
        if (active == null) return;

        Object controller = active;

        Platform.runLater(() -> {
            if (controller instanceof OrderController orderUI) {
                orderUI.showUserReservations((List<Reservation>) list);
            }
            else if (controller instanceof CancelReservationController cancelUI) {
                cancelUI.showUserReservations((List<Reservation>) list);
            }
            else {
                System.out.println("Unknown controller type for reservations handler.");
            }
        });
    }
}

