package commands;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
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

        // Attempt to update the table through the controller
        boolean success = controller.updateTable(req.getTableId(), req.getSeats());

        // Construct a response based on the outcome of the update
        ServerResponse response = new ServerResponse(
            success,
            null,
            success ? "Table updated." : "Failed to update table."
        );

        try {
            // Wrap the response in a Message object and send it to the client
            client.sendToClient(new Message(ActionType.UPDATE_TABLE, response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}