package commands;

import server.Command;
import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import messages.UpdateSpecialDateRequest;
import Entities.SpecialDates;
import common.Message;
import common.ServerResponse;
import enums.ActionType;

/**
 * Command implementation for updating an existing special date entry in the restaurant's calendar.
 * This class handles requests to modify details of holidays or closures, such as their 
 * timing or description, and ensures the client receives updated settings.
 */
public class UpdateSpecialDateCommand implements Command {

    /**
     * Controller responsible for managing restaurant configurations and schedule overrides.
     */
    private final RestaurantSettingsController controller =
            new RestaurantSettingsController();

    /**
     * Executes the update logic for a special date.
     * Extracts new details from the UpdateSpecialDateRequest, creates a new SpecialDates entity, 
     * and invokes the controller to replace the old record. Refreshes internal settings 
     * before notifying the client of the result.
     *
     * @param data   the UpdateSpecialDateRequest containing the old date and new details
     * @param client the connection to the client that issued the update request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof UpdateSpecialDateRequest req)) {
            sendError(client, "Invalid special date update request");
            return;
        }

        // Map the request data to a new SpecialDates entity
        SpecialDates updated = new SpecialDates(
                req.getOpeningTime(),
                req.getClosingTime(),
                req.getDate(),
                req.getDescription()
        );

        // Perform the update via the controller
        boolean ok = controller.updateSpecialDate(req.getOldDate(), updated);

        // Refresh internal state to ensure accuracy of the next settings retrieval
        controller.getAllWeeklyOpeningHours();
        controller.getAllSpecialDates();

        if (!ok) {
            sendError(client, "Failed to update special date");
            return;
        }

        sendSuccess(client, "Special date updated successfully");
    }

    /**
     * Sends a successful response back to the client including the updated restaurant settings.
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