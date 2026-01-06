package handlers;

import java.io.Serializable;

import Controllers.RestaurantSettingsController;
import Entities.RestaurantSettings;

public class UpdateSpecialDateHandler implements ResponseHandler{
	
	@Override
    public void handle(Object data) {
        RestaurantSettingsController controller = ClientHandler.getClient().getActiveRestaurantSettingsController();
        controller.loadRestaurantSettings((RestaurantSettings) data);
    }
}
