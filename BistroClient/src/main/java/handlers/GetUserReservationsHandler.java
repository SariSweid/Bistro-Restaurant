package handlers;

import java.util.List;

import Controllers.CancelReservationController;
import Entities.Reservation;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class GetUserReservationsHandler implements ResponseHandler {

	@Override
	public void handle(Object data) {
	    if (data instanceof List<?> list) {
	        System.out.println("[GetUserReservationsHandler] received " + list.size() + " reservations");
//	        for (Object obj : list) {
//	            if (obj instanceof Reservation r) {
//	                System.out.println("Reservation ID: " + r.getReservationID() +
//	                                   ", Date: " + r.getReservationDate() +
//	                                   ", Time: " + r.getReservationTime() +
//	                                   ", Guests: " + r.getNumOfGuests());
//	            }
//	        }
	        if (ClientHandler.getClient().getActiveCancelController() != null) {
	            ClientHandler.getClient().getActiveCancelController().showUserReservations(
	                (List<Reservation>) list
	            );
	        }
	    }
	}
}
