package commands;

import common.Message;
import enums.ActionType;
import logicControllers.RestaurantSettingsController;
import server.Command;
import src.ocsf.server.ConnectionToClient;
import Entities.RestaurantSettings;

/**
 * Command implementation for retrieving the current restaurant configuration.
 * This class fetches all relevant settings, including weekly opening hours and 
 * special dates (holidays/closures), ensuring the client has the most up-to-date 
 * operational information.
 */
public class GetRestaurantSettingsCommand implements Command {

    /**
     * Controller responsible for aggregating restaurant settings and schedule data.
     */
    private final RestaurantSettingsController controller = new RestaurantSettingsController();

    /**
     * Executes the retrieval logic for restaurant settings.
     * The process involves refreshing the internal cache of opening hours and 
     * special dates via the controller, constructing a RestaurantSettings 
     * object, and transmitting it back to the client.
     *
     * @param data   the data payload (not explicitly used in this command)
     * @param client the connection to the client requesting the settings
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            // Refresh the controller's data from the database to ensure accuracy
            controller.getAllWeeklyOpeningHours();  
            controller.getAllSpecialDates();     
            
            // Aggregate the data into a single settings entity
            RestaurantSettings settings = controller.getRestaurantSettings();
            
            // Wrap the settings in a Message and send to the client
            client.sendToClient(new Message(ActionType.GET_RESTAURANT_SETTINGS, settings));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}