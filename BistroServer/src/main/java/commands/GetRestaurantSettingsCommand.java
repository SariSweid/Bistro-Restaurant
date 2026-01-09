package commands;

import common.Message;
import enums.ActionType;
import logicControllers.RestaurantSettingsController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class GetRestaurantSettingsCommand implements Command {

    private final RestaurantSettingsController controller = new RestaurantSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
       
        Object settings = controller.getAllWeeklyOpeningHours(); 
        try {
            client.sendToClient(new Message(ActionType.GET_RESTAURANT_SETTINGS, settings));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
