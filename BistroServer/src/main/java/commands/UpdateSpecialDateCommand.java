package commands;

import server.Command;
import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import messages.UpdateSpecialDateRequest;
import Entities.SpecialDates;
import common.Message;
import common.ServerResponse;
import enums.ActionType;

public class UpdateSpecialDateCommand implements Command {

    private final RestaurantSettingsController controller =
            new RestaurantSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof UpdateSpecialDateRequest req)) {
            sendError(client, "Invalid special date update request");
            return;
        }

        SpecialDates updated = new SpecialDates(
                req.getOpeningTime(),
                req.getClosingTime(),
                req.getDate(),
                req.getDescription()
        );

        boolean ok = controller.updateSpecialDate(req.getOldDate(), updated);

        controller.getAllWeeklyOpeningHours();
        controller.getAllSpecialDates();

        if (!ok) {
            sendError(client, "Failed to update special date");
            return;
        }

        sendSuccess(client, "Special date updated successfully");
    }

    private void sendSuccess(ConnectionToClient client, String msg) {
        try {
            client.sendToClient(new Message(
                    ActionType.GET_RESTAURANT_SETTINGS,
                    new ServerResponse(true,
                            controller.getRestaurantSettings(),
                            msg)
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void sendError(ConnectionToClient client, String msg) {
        try {
            client.sendToClient(new Message(
                    ActionType.GET_RESTAURANT_SETTINGS,
                    new ServerResponse(false, null, msg)
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
