package commands;

import messages.UpdateReservationRequest;
import logicControllers.ReservationController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for updating an existing reservation's details.
 * This class facilitates communication between the client and the ReservationController
 * to modify reservation attributes such as date, time, or number of guests.
 */
public class UpdateReservationCommand implements Command {
	
    /**
     * Controller responsible for managing reservation-related business logic and database updates.
     */
    private ReservationController reservationController = new ReservationController();
    
    /**
     * Executes the reservation update operation.
     * Extracts the update details from the UpdateReservationRequest, invokes the 
     * business logic layer to persist changes, and returns a status string 
     * ("UPDATE_OK" or "UPDATE_FAIL") to the client.
     *
     * @param data   the UpdateReservationRequest object containing the modified reservation data
     * @param client the connection to the client that issued the update request
     */
    @Override	
    public void execute(Object data, ConnectionToClient client) {
    		
        UpdateReservationRequest req = (UpdateReservationRequest) data;
        
        // Perform the update using business logic
        boolean success = reservationController.updateReservation(req);

        try {
            // Send simple status response back to the client
            client.sendToClient(
                success ? "UPDATE_OK" : "UPDATE_FAIL"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}