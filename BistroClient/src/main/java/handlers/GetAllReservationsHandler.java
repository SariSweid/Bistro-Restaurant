package handlers;

import java.util.List;

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

            // Store the list in ClientHandler
            ClientHandler.getClient().setAllReservationsList((List<Reservation>) list);

            System.out.println("Reservations updated in ClientHandler.");
        });
    }
}

