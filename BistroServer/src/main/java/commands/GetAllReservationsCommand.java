package commands;

import java.util.List;
import Entities.Reservation;
import logicControllers.ReservationController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class GetAllReservationsCommand implements Command {
	
	// Controller responsible for reservation business logic
	private ReservationController reservationController = new ReservationController();
	
	/**
	 * Command responsible for handling GET_ALL_RESERVATIONS requests.
	 */
	@Override
	public void execute(Object data, ConnectionToClient client) {
	    try {
	        // Retrieve all reservations
	        List<Reservation> reservations = reservationController.getAllReservations();

	        // Send a Message object to the client
	        client.sendToClient(new common.Message(
	            enums.ActionType.GET_ALL_RESERVATIONS,
	            reservations
	        ));

	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            client.sendToClient(new common.Message(
	                enums.ActionType.GET_ALL_RESERVATIONS,
	                "SERVER_ERROR"
	            ));
	        } catch (Exception ignored) {}
	    }
	}

	
}
