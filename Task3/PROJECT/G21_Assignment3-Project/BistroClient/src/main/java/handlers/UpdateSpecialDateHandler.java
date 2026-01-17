package handlers;

import Controllers.RestaurantSettingsController;
import Entities.RestaurantSettings;

/**
 * Handles server responses related to updating special dates in the restaurant settings.
 * This handler forwards the received {@link RestaurantSettings} data to the
 * active {@link RestaurantSettingsController} to update the UI accordingly.
 */
public class UpdateSpecialDateHandler implements ResponseHandler {

    /**
     * Processes the server response containing updated restaurant settings
     * that include special dates. The settings are passed to the active
     * {@link RestaurantSettingsController} to refresh the displayed data.
     *
     * @param data the updated restaurant settings, expected to be of type
     *             {@link RestaurantSettings}
     */
    @Override
    public void handle(Object data) {
        RestaurantSettingsController controller = ClientHandler.getClient().getActiveRestaurantSettingsController();
        controller.loadRestaurantSettings((RestaurantSettings) data);
    }
}
