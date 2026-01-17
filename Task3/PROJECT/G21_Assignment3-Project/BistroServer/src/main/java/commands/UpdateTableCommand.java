package commands;

import common.ServerResponse;
import logicControllers.TableSettingsController;
import messages.UpdateTableRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for updating an existing table's configuration.
 * This class handles requests to modify table attributes, such as the number of seats,
 * and communicates the result of the update back to the client.
 */
public class UpdateTableCommand implements Command {

    /**
     * Controller responsible for managing table configurations and database updates.
     */
    private final TableSettingsController controller = new TableSettingsController();

    /**
     * Executes the table update logic.
     * Extracts the table ID and new seat count from the UpdateTableRequest,
     * invokes the update process via the controller, and returns a success 
     * or failure status to the client.
     *
     * @param data   the UpdateTableRequest containing the table ID and new capacity
     * @param client the connection to the client that requested the update
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        // Cast the incoming data to the specific request type
        UpdateTableRequest req = (UpdateTableRequest) data;

        System.out.println("DEBUG 1: Starting Conflict Check...");
        // This finds the people who had more seats than the new updates ones
        String conflictReport = controller.handleConflicts(req.getTableId(), req.getSeats(), false);
        // Attempt to update the table through the controller
        System.out.println("DEBUG 2: Conflict Report is: " + (conflictReport == null ? "NULL" : "GENERATED"));
        boolean success = controller.updateTable(req.getTableId(), req.getSeats());

        ServerResponse response = new common.ServerResponse(
                success, 
                null, 
                success ? "Table updated." : "Update failed."
            );

        try {
            synchronized(client) {
                // Always send the Update result first
                client.sendToClient(new common.Message(enums.ActionType.UPDATE_TABLE, response));
                
                // Only send the conflict report if it exists
                if (success && conflictReport != null) {
                    
                    Thread.sleep(50); 
                    System.out.println("DEBUG 3: Sending Conflict Message...");
                    client.sendToClient(new common.Message(enums.ActionType.RESERVATION_AFFECTED_BY_TABLE, conflictReport));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}