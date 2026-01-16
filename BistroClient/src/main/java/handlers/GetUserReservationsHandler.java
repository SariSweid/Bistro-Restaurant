package handlers;

import java.util.List;
import Controllers.BaseDisplayController;
import Controllers.SubscriberController;
import Entities.Reservation;
import javafx.application.Platform;

/**
 * Handles responses that contain a list of reservations for a user.
 * 
 * This handler is responsible for updating UI controllers that display
 * reservation data, including general display controllers and the
 * subscriber dashboard controller.
 */
public class GetUserReservationsHandler implements ResponseHandler {

    /**
     * Processes the response data received from the server.
     * 
     * If the data is a list of reservations, the method forwards it to:
     * - The active display controller to show reservations in the UI.
     * - The subscriber controller, if active, to trigger reservation-related logic
     *   such as notifications or popups.
     * 
     * All UI updates are executed on the JavaFX Application Thread.
     *
     * @param data the response object expected to contain a list of Reservation objects
     */
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
