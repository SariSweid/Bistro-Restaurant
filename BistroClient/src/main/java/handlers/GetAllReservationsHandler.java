package handlers;

import java.util.List;

import Entities.Reservation;
import client.GuestReservationUI;
import javafx.application.Platform;

/**
 * Handles the response from the server for GET_ALL_RESERVATIONS requests.
 * Updates the table in the GuestReservationUI.
 */
public class GetAllReservationsHandler implements ResponseHandler {

    private final GuestReservationUI ui;

    public GetAllReservationsHandler(GuestReservationUI ui) {
        this.ui = ui;
    }

	@SuppressWarnings("unchecked")
	@Override
    public void handle(Object data) {
        // The server should send a List<Reservation>
		// Always update JavaFX UI on the Application Thread
        Platform.runLater(() -> {
            if (data instanceof List<?> list) {
                // Cast safely
                ui.displayAllReservations((List<Reservation>) list);
	        } else {
	            // Fallback: show an error message in the UI
	            ui.showMessage("Failed to load reservations.");
	        }
        });
	}
}