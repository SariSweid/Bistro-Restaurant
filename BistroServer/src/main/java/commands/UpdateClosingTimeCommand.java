package commands;

import server.Command;
import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import messages.updateRegularClosingTimeRequest;
import Entities.WeeklyOpeningHours;
import common.Message;
import common.ServerResponse;
import enums.ActionType;

/**
 * Command implementation for updating the standard closing time for a specific day of the week.
 * This class handles requests to modify the restaurant's routine schedule by updating 
 * the WeeklyOpeningHours record and refreshing the overall restaurant settings.
 */
public class UpdateClosingTimeCommand implements Command {

    /**
     * Controller responsible for managing restaurant configuration and weekly schedules.
     */
    private final RestaurantSettingsController controller =
            new RestaurantSettingsController();

    /**
     * Executes the update logic for the restaurant's closing time.
     * Validates the request, retrieves the existing schedule for the day, updates the closing 
     * time, and persists the changes. It then triggers a full refresh of settings 
     * before notifying the client of the result.
     *
     * @param data   the updateRegularClosingTimeRequest containing the target day and new time
     * @param client the connection to the client that issued the update request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof updateRegularClosingTimeRequest req)) {
            sendError(client, "Invalid closing time request");
            return;
        }

        // Retrieve existing schedule for the specified day
        WeeklyOpeningHours current = controller.getOpeningHoursForDay(req.getDay());
        if (current == null) {
            sendError(client, "Day not found");
            return;
        }

        // Apply the new closing time and persist the update
        current.setClosingTime(req.getClosingTime());
        boolean ok = controller.createOrUpdateWeeklyOpeningHours(current);

        // Refresh internal state to ensure the returned settings are up to date
        controller.getAllWeeklyOpeningHours();
        controller.getAllSpecialDates();

        if (!ok) {
            sendError(client, "Failed to update closing time");
            return;
        }

        sendSuccess(client, "Closing time updated");
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
                new ServerResponse(true, controller.getRestaurantSettings(), msg)
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