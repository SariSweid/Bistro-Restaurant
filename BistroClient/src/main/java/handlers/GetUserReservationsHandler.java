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

	    Platform.runLater(() -> {
	        // Always update CancelReservationController
	        CancelReservationController cancelUI = (CancelReservationController) ClientHandler.getClient().getActiveCancelController();
	        if (cancelUI != null) {
	            cancelUI.showUserReservations((List<Reservation>) list);
	        }

	        // Always update OrderController
	        OrderController orderUI = ClientHandler.getClient().getOrderController();
	        if (orderUI != null) {
	            orderUI.showUserReservations((List<Reservation>) list);
	        }
	    });
	}

}

