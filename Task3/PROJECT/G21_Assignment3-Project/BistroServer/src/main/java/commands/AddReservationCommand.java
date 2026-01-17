package commands;

import java.io.IOException;

import Entities.Reservation;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.ReservationController;
import messages.AddReservationRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for creating a new reservation in the system.
 * This class processes reservation requests by validating the data through 
 * the ReservationController and returning the approval or denial status to the client.
 */
public class AddReservationCommand implements Command {
	
    /**
     * Controller responsible for managing reservation business logic and persistence.
     */
    private ReservationController reservationController = new ReservationController();
    
    /**
     * Executes the logic to add a new reservation.
     * Extracts the reservation entity from the AddReservationRequest, attempts 
     * to save it via the controller, and sends a ServerResponse back to the 
     * client indicating if the reservation was approved or denied.
     *
     * @param data   the AddReservationRequest containing the new reservation details
     * @param client the connection to the client that issued the request
     */
	@Override
	public void execute(Object data, ConnectionToClient client) {
		
		try {
	        if (!(data instanceof AddReservationRequest req)) return;

	        Reservation r = req.getReservation();

	        // Attempt to add the reservation via the logic controller
	        boolean success = reservationController.addReservation(r);

	        // Prepare the response based on the outcome
	        ServerResponse res = success
	            ? new ServerResponse(true, r, "Reservation approved")
	            : new ServerResponse(false, null, "Reservation denied");

	        // Send the result back to the client
	        client.sendToClient(new Message(ActionType.ADD_RESERVATION, res));

	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
				client.sendToClient(new Message(ActionType.ADD_RESERVATION,
				        new ServerResponse(false, null, "Server error")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    }
    }
}