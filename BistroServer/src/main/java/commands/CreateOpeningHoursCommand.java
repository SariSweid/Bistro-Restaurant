package commands;

import java.io.IOException;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.RestaurantSettingsController;
import messages.CreateRegularOpeningHoursRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;
import Entities.WeeklyOpeningHours;

public class CreateOpeningHoursCommand implements Command {

    private final RestaurantSettingsController controller = new RestaurantSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            if (!(data instanceof CreateRegularOpeningHoursRequest req)) return;

            WeeklyOpeningHours wh = req.getHours();

            boolean success = controller.createOrUpdateWeeklyOpeningHours(wh);
            String message = controller.isDayExisting(wh.getDay()) 
                                ? "Day updated successfully" 
                                : "Day created successfully";

            ServerResponse response = new ServerResponse(success, wh, message);
            client.sendToClient(new Message(ActionType.CREATE_OPENING_HOURS, response));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                ServerResponse response = new ServerResponse(false, null, "Error creating opening hours");
                client.sendToClient(new Message(ActionType.CREATE_OPENING_HOURS, response));
            } catch (IOException ignored) {}
        }
    }
}
