package handlers;

import java.util.List;
import Controllers.BaseDisplayController;
import Controllers.SubscriberController;
import Entities.Reservation;
import javafx.application.Platform;

public class GetUserReservationsHandler implements ResponseHandler {

    @SuppressWarnings("unchecked")
    @Override
    public void handle(Object data) {
        if (!(data instanceof List<?> list)) return;

        System.out.println("[GetUserReservationsHandler] received " + list.size() + " reservations");
        List<Reservation> reservations = (List<Reservation>) list;

        // Handle display Controllers
        BaseDisplayController displayController = ClientHandler.getClient().getActiveDisplayController();
        if (displayController != null) {
            Platform.runLater(() -> {
                displayController.showReservations(reservations);
            });
        }

        // Handle Subscriber Dashboard (For the Popup)
        // Check if the current active controller is the Subscriber dash
        Object activeResController = ClientHandler.getClient().getActiveReservationController();
        
        if (activeResController instanceof SubscriberController) {
            System.out.println("[GetUserReservationsHandler] SubscriberController found! Triggering notification check.");
            Platform.runLater(() -> {
                ((SubscriberController) activeResController).onReservationsReceived(reservations);
            });
        }
    }
}