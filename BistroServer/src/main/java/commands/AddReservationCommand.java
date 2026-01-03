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

public class AddReservationCommand implements Command {
	
	// Controller responsible for reservation business logic
    private ReservationController reservationController = new ReservationController();
    
    
    /**
     * Command responsible for handling ADD_RESERVATION requests.
     */
	@Override
	public void execute(Object data, ConnectionToClient client) {
		
		try {
	        if (!(data instanceof AddReservationRequest req)) return;

	        Reservation r = req.getReservation();

	        boolean success = reservationController.addReservation(r);

	        ServerResponse res = success
	            ? new ServerResponse(true, r, "Reservation approved")
	            : new ServerResponse(false, null, "Reservation denied");

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
