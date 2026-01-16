package commands;

import logicControllers.ReservationController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class MarkNotifiedCommand implements Command {
    
    // Controller responsible for reservation business logic
    private ReservationController resController = new ReservationController();

    /**
     * Command responsible for handling MARK_NOTIFIED requests.
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        int reservationID = (int) data;
        
        try {
            // Call business logic to update the isNotified column to 1
            boolean success = resController.markReservationAsNotified(reservationID);
            
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