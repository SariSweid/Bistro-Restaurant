package commands;

import server.Command;

import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import messages.AddSpecialDateRequest;
import common.Message;
import common.ServerResponse;
import enums.ActionType;

/**
 * Command implementation for adding a special date (such as a holiday or planned closure) 
 * to the restaurant's calendar. This class handles the persistence of the new date 
 * and refreshes the settings to ensure the client receives an updated schedule.
 */
public class AddSpecialDateCommand implements Command {

    /**
     * Controller responsible for managing restaurant configurations, opening hours, and special dates.
     */
    private final RestaurantSettingsController controller =
            new RestaurantSettingsController();

    /**
     * Executes the logic to add a new special date.
     * Validates the incoming request data, persists the special date via the controller, 
     * and refreshes the system settings before sending a success or failure response.
     *
     * @param data   the AddSpecialDateRequest containing the date and description
     * @param client the connection to the client that issued the request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof AddSpecialDateRequest sd)) {
            sendError(client, "Invalid special date data");
            return;
        }

        // Add the special date through the controller logic
        boolean ok = controller.addSpecialDate(sd);

        // Refresh internal controller state to sync opening hours and dates
        controller.getAllWeeklyOpeningHours();
        controller.getAllSpecialDates();

        if (!ok) {
            sendError(client, "Failed to add special date");
            return;
        }

        sendSuccess(client, "Special date added successfully");
    }

    /**
     * Sends a successful response back to the client including updated restaurant settings.
     *
     * @param client the client connection
     * @param msg    the success message to be sent
     */
    private void sendSuccess(ConnectionToClient client, String msg) {
        try {
            client.sendToClient(new Message(
                    ActionType.GET_RESTAURANT_SETTINGS,
                    new ServerResponse(true,
                            controller.getRestaurantSettings(),
                            msg)
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Sends an error response back to the client.
     *
     * @param client the client connection
     * @param msg    the error description to be sent
     */
    private void sendError(ConnectionToClient client, String msg) {
        try {
            client.sendToClient(new Message(
                    ActionType.GET_RESTAURANT_SETTINGS,
                    new ServerResponse(false, null, msg)
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }
}