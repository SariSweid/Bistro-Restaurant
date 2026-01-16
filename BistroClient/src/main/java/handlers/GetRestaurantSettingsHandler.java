package handlers;

import Controllers.RestaurantSettingsController;
import Entities.RestaurantSettings;

/**
 * Handles the server response containing restaurant settings data.
 * Updates the active RestaurantSettingsController and synchronizes
 * the client-side RestaurantSettings singleton with the received values.
 */
public class GetRestaurantSettingsHandler implements ResponseHandler {

    /**
     * Processes the server response data.
     * Expects a RestaurantSettings object and updates both the UI controller
     * and the shared RestaurantSettings instance used on the client side.
     *
     * @param data the server response data, expected to be a RestaurantSettings instance
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof RestaurantSettings settings)) return;

        RestaurantSettingsController controller =
                ClientHandler.getClient().getActiveRestaurantSettingsController();
        if (controller != null) {
            controller.loadRestaurantSettings(settings);
        }

        RestaurantSettings shared = RestaurantSettings.getInstance();
        shared.setWeeklyOpeningHours(settings.getWeeklyOpeningHours());
        shared.setSpecialDates(settings.getSpecialDates());
        shared.setMaxTables(settings.getMaxTables());
        shared.setReservationDurationHours(settings.getReservationDurationHours());
    }
}
