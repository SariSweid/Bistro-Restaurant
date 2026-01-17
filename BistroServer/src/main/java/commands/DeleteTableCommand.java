package commands;


import common.ServerResponse;
import logicControllers.TableSettingsController;
import messages.DeleteTableRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for removing a table from the system.
 * Handles reservation conflicts by unassigning affected bookings before deletion.
 */
public class DeleteTableCommand implements Command {

    private final TableSettingsController controller = new TableSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        DeleteTableRequest req = (DeleteTableRequest) data;

        // Check for affected reservations and update them
        // We pass 0 for capacity and true for isDeletion
        String conflictReport = controller.handleConflicts(req.getTableId(), 0, true);

        // Proceed with physical deletion from the database
        boolean success = controller.deleteTable(req.getTableId());

        ServerResponse response = new ServerResponse(
            success,
            null,
            success ? "Table deleted successfully." : "Failed to delete table."
        );

        try {
            synchronized(client) {
                // Always send the Delete result first
                client.sendToClient(new common.Message(enums.ActionType.DELETE_TABLE, response));
                
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