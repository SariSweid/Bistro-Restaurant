package handlers;

import Controllers.RestaurantSettingsController;
import Entities.RestaurantSettings;

public class UpdateRegularOpeningTimeHandler implements ResponseHandler {
	@Override
    public void handle(Object data) {
        RestaurantSettingsController controller = ClientHandler.getClient().getActiveRestaurantSettingsController();
        controller.loadRestaurantSettings((RestaurantSettings) data);
    }

}
