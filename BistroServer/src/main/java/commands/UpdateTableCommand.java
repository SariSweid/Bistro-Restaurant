package commands;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.TableSettingsController;
import messages.UpdateTableRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class UpdateTableCommand implements Command {

    private final TableSettingsController controller = new TableSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        UpdateTableRequest req = (UpdateTableRequest) data;

        boolean success = controller.updateTable(req.getTableId(), req.getSeats());

        ServerResponse response = new ServerResponse(
            success,
            null,
            success ? "Table updated." : "Failed to update table."
        );

        try {
            client.sendToClient(new Message(ActionType.UPDATE_TABLE, response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
