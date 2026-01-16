package handlers;

import java.util.List;

import Controllers.BaseDisplayController;
import Entities.Reservation;
import javafx.application.Platform;

/**
 * Handles the server response for retrieving all reservations.
 * 
 * Updates the ClientHandler with the latest list of reservations and notifies
 * the active BaseDisplayController to display the updated reservations if one exists.
 */
public class GetAllReservationsHandler implements ResponseHandler {

    /**
     * Processes the server response containing the list of reservations.
     * 
     * @param data the response object received from the server; expected to be a List of Reservation objects
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handle(Object data) {
        Platform.runLater(() -> {

            if (!(data instanceof List<?> list)) {
                System.out.println("Failed to load reservations.");
                return;
            }
            
            List<Reservation> reservations = (List<Reservation>) list;

            // Store the list in ClientHandler
            ClientHandler.getClient().setAllReservationsList(reservations);

            System.out.println("Reservations updated in ClientHandler.");
            
            // Update the active display controller
            BaseDisplayController controller = ClientHandler.getClient().getActiveDisplayController();
            if (controller != null) {
                controller.showReservations(reservations);
            }

        });
    }
}
