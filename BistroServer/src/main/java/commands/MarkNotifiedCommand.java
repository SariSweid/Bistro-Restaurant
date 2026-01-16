package commands;

import logicControllers.ReservationController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for updating the notification status of a reservation.
 * This class is typically used to acknowledge that a customer has been informed 
 * of a system-level event, such as an automatic cancellation, by updating 
 * the 'isNotified' flag in the database.
 */
public class MarkNotifiedCommand implements Command {
    
    /**
     * Controller responsible for reservation business logic and persistence.
     */
    private ReservationController resController = new ReservationController();

    /**
     * Executes the logic to mark a reservation as notified.
     * Receives a reservation ID, invokes the controller to update the record 
     * in the database, and returns a simple status string ("MARK_NOTIFIED_OK" 
     * or "MARK_NOTIFIED_FAIL") to the client.
     *
     * @param data   the reservation ID (Integer) to be updated
     * @param client the connection to the client that issued the request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        int reservationID = (int) data;
        
        try {
            // Call business logic to update the notification flag
            boolean success = resController.markReservationAsNotified(reservationID);
            
            // Notify the client of the operation result
            if (success) {
                client.sendToClient("MARK_NOTIFIED_OK");
            } else {
                client.sendToClient("MARK_NOTIFIED_FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}