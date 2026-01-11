package handlers;

import Controllers.RestaurantSettingsController;
import Entities.RestaurantSettings;

public class GetRestaurantSettingsHandler implements ResponseHandler {
    @Override
    public void handle(Object data) {
        if (!(data instanceof RestaurantSettings settings)) return;

        // Update UI controller
        RestaurantSettingsController controller =
            ClientHandler.getClient().getActiveRestaurantSettingsController();
        if (controller != null) {
            controller.loadRestaurantSettings(settings);
        }

        // Update client-side singleton
        RestaurantSettings shared = RestaurantSettings.getInstance();
        shared.setWeeklyOpeningHours(settings.getWeeklyOpeningHours());
        shared.setSpecialDates(settings.getSpecialDates());
        shared.setMaxTables(settings.getMaxTables());
        shared.setReservationDurationHours(settings.getReservationDurationHours());
    }
}

