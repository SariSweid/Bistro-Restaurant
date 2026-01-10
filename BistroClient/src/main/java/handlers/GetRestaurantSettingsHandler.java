package handlers;

import Controllers.RestaurantSettingsController;
import Entities.RestaurantSettings;

public class GetRestaurantSettingsHandler implements ResponseHandler {

	@Override
	public void handle(Object data) {
	    System.out.println("Received RestaurantSettings from server: " + data);
	    if (data instanceof RestaurantSettings) {
	        RestaurantSettings settings = (RestaurantSettings) data;
	        RestaurantSettingsController controller = ClientHandler.getClient().getActiveRestaurantSettingsController();
	        if (controller != null) 
	            controller.loadRestaurantSettings(settings);
	    } else {
	        System.out.println("Data is not RestaurantSettings!");
	    }
	}

}

