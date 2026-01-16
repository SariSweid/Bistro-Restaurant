package handlers;

import Controllers.RestaurantSettingsController;

/**
 * Handles the server response for deleting a special date from the restaurant settings.
 * 
 * When a special date is deleted, this handler refreshes the restaurant settings
 * by requesting the updated data from the server and updating the active 
 * RestaurantSettingsController if it exists.
 */
public class DeleteSpecialDateHandler implements ResponseHandler {

    /**
     * Processes the server response for deleting a special date.
     * If the active RestaurantSettingsController is available, it refreshes
     * the restaurant settings by requesting updated data from the server.
     *
     * @param data the response object received from the server (ignored in this handler)
     */
    @Override
    public void handle(Object data) { 
        RestaurantSettingsController controller = ClientHandler.getClient().getActiveRestaurantSettingsController();
        if (controller != null) {
            ClientHandler.getClient().getRestaurantSettings();
        }
    }
}
