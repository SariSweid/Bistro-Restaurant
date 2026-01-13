package handlers;

import Controllers.RestaurantSettingsController;
import java.time.LocalDate;


public class DeleteSpecialDateHandler implements ResponseHandler {
	@Override
	public void handle( Object data) { 
		RestaurantSettingsController controller = ClientHandler.getClient().getActiveRestaurantSettingsController();
		if(controller!=null) {
			ClientHandler.getClient().getRestaurantSettings();
		}
	}
}
