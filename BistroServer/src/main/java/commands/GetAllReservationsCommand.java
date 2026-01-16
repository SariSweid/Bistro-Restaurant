package commands;

import java.util.List;
import Entities.Reservation;
import logicControllers.ReservationController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for retrieving all reservation records from the system.
 * This class acts as a bridge between the client request and the ReservationController
 * to fetch and return the complete list of reservations.
 */
public class GetAllReservationsCommand implements Command {
	
	/**
	 * Controller responsible for reservation business logic and data retrieval.
	 */
	private ReservationController reservationController = new ReservationController();
	
	/**
	 * Executes the request to fetch all reservations.
	 * It calls the business logic layer to get the data and packages the result
	 * into a Message object for transmission back to the client.
	 *
	 * @param data   the data sent from the client (not used in this specific command)
	 * @param client the connection instance representing the client that requested the data
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
	            // Send an error message back to the client in case of failure
	            client.sendToClient(new common.Message(
	                enums.ActionType.GET_ALL_RESERVATIONS,
	                "SERVER_ERROR"
	            ));
	        } catch (Exception ignored) {}
	    }
	}

}