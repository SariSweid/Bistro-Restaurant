package handlers;

import Controllers.RestaurantSettingsController;
import Entities.RestaurantSettings;

public class GetRestaurantSettingsHandler implements ResponseHandler {

	@Override
    public void handle(Object data) {
        RestaurantSettings settings = (RestaurantSettings) data;
        RestaurantSettingsController controller = ClientHandler.getClient().getActiveRestaurantSettingsController();
        if (controller != null) 
        	controller.loadRestaurantSettings(settings);
    }//
}

