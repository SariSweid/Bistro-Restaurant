package commands;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.TableSettingsController;
import messages.InsertTableRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class InsertTableCommand implements Command {

    private final TableSettingsController controller = new TableSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        InsertTableRequest req = (InsertTableRequest) data;

        boolean success = controller.insertTable(req.getTableId(), req.getSeats());

        ServerResponse response = new ServerResponse(
            success,
            null,
            success ? "Table inserted." : "Table ID already exists."
        );

        try {
            client.sendToClient(new Message(ActionType.INSERT_TABLE, response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
