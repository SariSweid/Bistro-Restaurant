package handlers;

import Controllers.RestaurantSettingsController;
import Entities.RestaurantSettings;

/**
 * Handles server responses for updating the regular opening times of the restaurant.
 * When a response is received, it updates the RestaurantSettingsController
 * with the new RestaurantSettings data.
 */
public class UpdateRegularOpeningTimeHandler implements ResponseHandler {

    /**
     * Handles the server response containing updated restaurant settings.
     * The method retrieves the active RestaurantSettingsController from the client
     * and passes the received RestaurantSettings object to it for updating the UI.
     *
     * @param data the updated RestaurantSettings object received from the server
     */
    @Override
    public void handle(Object data) {
        RestaurantSettingsController controller = ClientHandler.getClient().getActiveRestaurantSettingsController();
        controller.loadRestaurantSettings((RestaurantSettings) data);
    }
}
