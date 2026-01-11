package commands;

import server.Command;
import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import messages.updateRegularClosingTimeRequest;
import Entities.WeeklyOpeningHours;
import common.Message;
import common.ServerResponse;
import enums.ActionType;

public class UpdateClosingTimeCommand implements Command {

    private final RestaurantSettingsController controller =
            new RestaurantSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof updateRegularClosingTimeRequest req)) {
            sendError(client, "Invalid closing time request");
            return;
        }

        WeeklyOpeningHours current = controller.getOpeningHoursForDay(req.getDay());
        if (current == null) {
            sendError(client, "Day not found");
            return;
        }

        current.setClosingTime(req.getClosingTime());
        boolean ok = controller.updateWeeklyOpeningHours(current);

        controller.getAllWeeklyOpeningHours();
        controller.getAllSpecialDates();

        if (!ok) {
            sendError(client, "Failed to update closing time");
            return;
        }

        sendSuccess(client, "Closing time updated");
    }

    private void sendSuccess(ConnectionToClient client, String msg) {
        try {
            client.sendToClient(new Message(
                ActionType.GET_RESTAURANT_SETTINGS,
                new ServerResponse(true, controller.getRestaurantSettings(), msg)
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

