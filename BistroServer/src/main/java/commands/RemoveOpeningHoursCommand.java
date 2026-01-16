package commands;

import java.io.IOException;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.RestaurantSettingsController;
import messages.DeleteOpeningHoursRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for removing a set of weekly opening hours from the system.
 * This class processes requests to delete the operating hours for a specific day,
 * effectively resetting or clearing the schedule for that day in the restaurant's settings.
 */
public class RemoveOpeningHoursCommand implements Command {
    
    /**
     * Controller responsible for managing restaurant configuration and schedule deletions.
     */
    private final RestaurantSettingsController controller = new RestaurantSettingsController();

    /**
     * Executes the logic to remove opening hours for a specific day.
     * Extracts the day from the DeleteOpeningHoursRequest, invokes the controller to 
     * perform the deletion, and returns a ServerResponse indicating the outcome.
     *
     * @param data   the DeleteOpeningHoursRequest containing the day to be removed
     * @param client the connection to the client that issued the request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof DeleteOpeningHoursRequest wh)) return;

        // Attempt to remove the schedule for the specified day via the controller
        boolean success = controller.removeWeeklyOpeningHours(wh.getDay());

        try {
            // Send the result of the deletion back to the client
            client.sendToClient(new Message(ActionType.REMOVE_OPENING_HOURS,
                    new ServerResponse(success, wh, success ? "Deleted successfully" : "Failed to delete")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}