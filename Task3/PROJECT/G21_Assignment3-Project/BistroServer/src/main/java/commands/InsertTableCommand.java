package commands;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.TableSettingsController;
import messages.InsertTableRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for inserting a new table into the restaurant system.
 * This class handles the logic of adding physical tables to the database, ensuring 
 * that the table configuration (ID and seating capacity) is properly recorded.
 */
public class InsertTableCommand implements Command {

    /**
     * Controller responsible for managing table-related operations and data persistence.
     */
    private final TableSettingsController controller = new TableSettingsController();

    /**
     * Executes the table insertion logic.
     * Extracts the table details from the InsertTableRequest, attempts to persist the 
     * new table via the controller, and returns a ServerResponse indicating whether 
     * the insertion was successful or if the table ID already exists.
     *
     * @param data   the InsertTableRequest object containing the new table ID and seat count
     * @param client the connection to the client that issued the insertion request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        // Cast the incoming data to the specific request type
        InsertTableRequest req = (InsertTableRequest) data;

        // Attempt to insert the new table into the system
        boolean success = controller.insertTable(req.getTableId(), req.getSeats());

        // Prepare the response based on the controller's result
        ServerResponse response = new ServerResponse(
            success,
            null,
            success ? "Table inserted." : "Table ID already exists."
        );

        try {
            // Send the response back to the client via the OCSF framework
            client.sendToClient(new Message(ActionType.INSERT_TABLE, response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}