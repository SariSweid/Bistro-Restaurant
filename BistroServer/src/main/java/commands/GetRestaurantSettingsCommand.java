package commands;

import common.Message;
import enums.ActionType;
import logicControllers.RestaurantSettingsController;
import server.Command;
import src.ocsf.server.ConnectionToClient;
import Entities.RestaurantSettings;

public class GetRestaurantSettingsCommand implements Command {

    private final RestaurantSettingsController controller = new RestaurantSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            
            controller.getAllWeeklyOpeningHours();  
            controller.getAllSpecialDates();     
            
            
           
            RestaurantSettings settings = controller.getRestaurantSettings();
            
            
            client.sendToClient(new Message(ActionType.GET_RESTAURANT_SETTINGS, settings));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
