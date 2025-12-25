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
            // Retrieve all reservations from business logic
            List<Reservation> reservations = reservationController.getAllReservations();

            // Send the list back to the client
            client.sendToClient(reservations);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient("SERVER_ERROR");
            } catch (Exception ignored) {}
        }
	}
	
}
