package commands;

import java.util.List;

import Entities.Table;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.TableController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class GetAllTablesCommand implements Command {

    private final TableController controller = new TableController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            List<Table> tables = controller.getAllTables();

            ServerResponse response = new ServerResponse(
                true,
                tables,
                "Tables retrieved successfully."
            );

            client.sendToClient(new Message(ActionType.GET_ALL_TABLES, response));

        } catch (Exception e) {
            e.printStackTrace();

            try {
                ServerResponse response = new ServerResponse(
                    false,
                    null,
                    "Failed to retrieve tables."
                );

                client.sendToClient(new Message(ActionType.GET_ALL_TABLES, response));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
