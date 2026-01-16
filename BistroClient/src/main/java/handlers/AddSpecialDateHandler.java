package handlers;

import Controllers.RestaurantSettingsController;
import Entities.RestaurantSettings;

/**
 * Handler for processing the response after adding a special date.
 * Updates the RestaurantSettingsController with the latest restaurant settings received from the server.
 */
public class AddSpecialDateHandler implements ResponseHandler {

    /**
     * Handles the server response after a special date is added.
     * Casts the data to RestaurantSettings and loads it into the active controller.
     *
     * @param data the response data from the server, expected to be of type RestaurantSettings
     */
    @Override
    public void handle(Object data) {
        RestaurantSettingsController controller = ClientHandler.getClient().getActiveRestaurantSettingsController();
        controller.loadRestaurantSettings((RestaurantSettings) data);
    }
}
