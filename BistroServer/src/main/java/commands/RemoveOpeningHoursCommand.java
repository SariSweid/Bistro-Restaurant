package commands;

import java.io.IOException;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.RestaurantSettingsController;
import messages.DeleteOpeningHoursRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class RemoveOpeningHoursCommand implements Command {
    private final RestaurantSettingsController controller = new RestaurantSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof DeleteOpeningHoursRequest wh)) return;

        boolean success = controller.removeWeeklyOpeningHours(wh.getDay());

        try {
            client.sendToClient(new Message(ActionType.REMOVE_OPENING_HOURS,
                    new ServerResponse(success, wh, success ? "Deleted successfully" : "Failed to delete")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
