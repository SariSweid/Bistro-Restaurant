package commands;

import messages.UpdateReservationRequest;
import logicControllers.ReservationController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class UpdateReservationCommand implements Command {
	
	// Controller responsible for reservation business logic
    private ReservationController reservationController = new ReservationController();
    
    /**
     * Executes the update reservation operation.
     *
     * @param data   UpdateReservationRequest object
     * @param client client that sent the request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
    		
        UpdateReservationRequest req = (UpdateReservationRequest) data;
        
        // Perform the update using business logic
        boolean success = reservationController.updateReservation(
                req.getReservationID(),
                req.getReservationDate(),
                req.getNumOfGuests()
        );

        try {
            client.sendToClient(
                success ? "UPDATE_OK" : "UPDATE_FAIL"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
