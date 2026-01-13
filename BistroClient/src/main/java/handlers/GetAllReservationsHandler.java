package handlers;

import java.util.List;

import Controllers.BaseDisplayController;
import Entities.Reservation;
import javafx.application.Platform;

public class GetAllReservationsHandler implements ResponseHandler {

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

