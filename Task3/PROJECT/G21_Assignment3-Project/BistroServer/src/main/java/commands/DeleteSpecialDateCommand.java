package commands;

import server.Command;

import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import messages.DeleteSpecialDateRequest;
import common.Message;
import common.ServerResponse;
import enums.ActionType;

/**
 * Command implementation for removing a special date (holiday or closure) from the restaurant's calendar.
 * This class handles the deletion of custom date-based settings and refreshes the current
 * restaurant settings to ensure the client has the most up-to-date schedule.
 */
public class DeleteSpecialDateCommand implements Command{
	
    /**
     * Controller responsible for managing restaurant-wide settings, including opening hours and special dates.
     */
    private final RestaurantSettingsController controller =
            new RestaurantSettingsController();
	
    /**
     * Executes the logic to delete a special date.
     * Validates the input data, invokes the deletion via RestaurantSettingsController,
     * and triggers a refresh of opening hours and special dates before responding to the client.
     *
     * @param data   expected to be an instance of DeleteSpecialDateRequest
     * @param client the connection to the client that issued the deletion request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof DeleteSpecialDateRequest req)) {
            sendError(client, "Invalid special date data");
            return;
        }

        // Attempt to remove the special date record
        boolean ok = controller.deleteSpecialDate(req.getDate());

        // Refresh local controller state to ensure the returned settings are current
        controller.getAllWeeklyOpeningHours();
        controller.getAllSpecialDates();

        if (!ok) {
            sendError(client, "Failed to delete special date");
            return;
        }

        sendSuccess(client, "Special date delete successfully");
    }
	
    /**
     * Sends a successful response back to the client, including the updated restaurant settings object.
     *
     * @param client the client connection
     * @param msg    the success message to be displayed or logged
     */
    private void sendSuccess(ConnectionToClient client, String msg) {
        try {
            client.sendToClient(new Message(ActionType.GET_RESTAURANT_SETTINGS, 
                    new ServerResponse(true, controller.getRestaurantSettings(),msg)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Sends a failure response back to the client.
     *
     * @param client the client connection
     * @param msg    the error message describing what went wrong
     */
    private void sendError(ConnectionToClient client, String msg) {
        try {
            client.sendToClient(new Message(ActionType.GET_RESTAURANT_SETTINGS,
                    new ServerResponse(false, null, msg)));
        } catch(Exception e){
            e.printStackTrace();
        }
    }
	
}