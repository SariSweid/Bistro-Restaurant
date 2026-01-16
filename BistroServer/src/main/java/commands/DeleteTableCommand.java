package commands;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.TableSettingsController;
import messages.DeleteTableRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for removing a table from the system configuration.
 * This class handles requests to delete a specific table entity, typically triggered 
 * from an administrative or floor management settings interface.
 */
public class DeleteTableCommand implements Command {

    /**
     * Controller responsible for managing table configurations and persistent storage updates.
     */
    private final TableSettingsController controller = new TableSettingsController();

    /**
     * Executes the table deletion logic.
     * Extracts the table ID from the request, invokes the deletion 
     * process via the controller, and returns a success or failure status to the client.
     * * @param data    the DeleteTableRequest containing the ID of the table to be removed
     * @param client  the connection to the client that requested the deletion
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        // Cast the incoming data to the specific request type
        DeleteTableRequest req = (DeleteTableRequest) data;

        // Attempt to delete the table through the controller
        boolean success = controller.deleteTable(req.getTableId());

        // Construct a response based on the outcome of the deletion
        ServerResponse response = new ServerResponse(
            success,
            null,
            success ? "Table deleted." : "Failed to delete table."
        );

        try {
            // Wrap the response in a Message object and send it to the client
            client.sendToClient(new Message(ActionType.DELETE_TABLE, response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}