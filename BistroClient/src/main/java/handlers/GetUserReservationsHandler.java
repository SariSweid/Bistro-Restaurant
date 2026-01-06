package handlers;

import java.util.List;
import Controllers.CancelReservationController;
import Entities.Reservation;
import javafx.application.Platform;


public class GetUserReservationsHandler implements ResponseHandler {

		@SuppressWarnings("unchecked")
		@Override
	    public void handle(Object data) {
	        if (!(data instanceof List<?> list)) return;

	        System.out.println("[GetUserReservationsHandler] received " + list.size() + " reservations");

	        Object controller = ClientHandler.getClient().getActiveCancelController();
	        if (controller == null) return;

	        Platform.runLater(() -> {
	            if (controller instanceof CancelReservationController subController) {
	                // Subscriber: show in table
	                subController.showUserReservations((List<Reservation>) list);
	                
	            } else {
	                System.out.println("Unknown cancel controller type for GetUserReservationsHandler.");
	            }
	        });
	    }
}
