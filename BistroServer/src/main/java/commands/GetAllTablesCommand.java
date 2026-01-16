package commands;

import java.util.List;

import Entities.Table;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.TableController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for retrieving a complete list of restaurant tables.
 * This class facilitates the communication between the client and the TableController
 * to provide table data, which is typically used for floor management or reservation seating.
 */
public class GetAllTablesCommand implements Command {

    /**
     * Controller responsible for table-related business logic and database access.
     */
    private final TableController controller = new TableController();

    /**
     * Executes the request to fetch all table records.
     * It interacts with the TableController to retrieve the list of Table entities
     * and wraps them in a ServerResponse inside a Message for the client.
     *
     * @param data   the data received from the client (not used in this command)
     * @param client the connection to the client that issued the request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            // Retrieve the list of all tables from the business logic layer
            List<Table> tables = controller.getAllTables();

            // Prepare a successful server response containing the table list
            ServerResponse response = new ServerResponse(
                true,
                tables,
                "Tables retrieved successfully."
            );

            // Send the packaged message to the client
            client.sendToClient(new Message(ActionType.GET_ALL_TABLES, response));

        } catch (Exception e) {
            e.printStackTrace();

            try {
                // Prepare a failure response in case of a server-side error
                ServerResponse response = new ServerResponse(
                    false,
                    null,
                    "Failed to delete table."
                );

                client.sendToClient(new Message(ActionType.GET_ALL_TABLES, response));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}