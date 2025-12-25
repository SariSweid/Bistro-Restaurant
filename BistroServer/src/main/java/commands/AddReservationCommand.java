package commands;

import Entities.Reservation;
import logicControllers.ReservationController;
import messages.AddReservationRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class AddReservationCommand implements Command {
	
	// Controller responsible for reservation business logic
    private ReservationController reservationController = new ReservationController();
    
    /**
     * Command responsible for handling ADD_RESERVATION requests.
     */
	@Override
	public void execute(Object data, ConnectionToClient client) {
		// Cast the data object to AddReservationRequest
        AddReservationRequest req = (AddReservationRequest) data;

        // Extract the Reservation object
        Reservation reservation = req.getReservation();

        // Call business logic to add the reservation
        boolean success = reservationController.addReservation(reservation);

        // Send response back to the client
        try {
            client.sendToClient(success ? "ADD_OK" : "ADD_FAIL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
