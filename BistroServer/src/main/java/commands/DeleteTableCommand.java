package commands;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.TableSettingsController;
import messages.DeleteTableRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class DeleteTableCommand implements Command {

    private final TableSettingsController controller = new TableSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        DeleteTableRequest req = (DeleteTableRequest) data;

        boolean success = controller.deleteTable(req.getTableId());

        ServerResponse response = new ServerResponse(
            success,
            null,
            success ? "Table deleted." : "Failed to delete table."
        );

        try {
            client.sendToClient(new Message(ActionType.DELETE_TABLE, response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
