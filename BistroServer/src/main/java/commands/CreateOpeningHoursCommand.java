package commands;

import java.io.IOException;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.RestaurantSettingsController;
import messages.CreateRegularOpeningHoursRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;
import Entities.WeeklyOpeningHours;

/**
 * Command implementation for creating or updating the restaurant's regular weekly opening hours.
 * This class processes requests to set operating times for specific days of the week, 
 * delegating the persistence and validation logic to the RestaurantSettingsController.
 */
public class CreateOpeningHoursCommand implements Command {

    /**
     * Controller responsible for managing restaurant configuration and schedule logic.
     */
    private final RestaurantSettingsController controller = new RestaurantSettingsController();

    /**
     * Executes the process of creating or updating opening hours.
     * It extracts the WeeklyOpeningHours from the request, determines if the operation 
     * is an update or a new creation, and transmits the resulting status back to the client.
     *
     * @param data   the CreateRegularOpeningHoursRequest containing the schedule details
     * @param client the connection to the client that issued the request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            // Validate that the incoming data matches the expected request type
            if (!(data instanceof CreateRegularOpeningHoursRequest req)) return;

            WeeklyOpeningHours wh = req.getHours();

            // Perform the creation or update in the database via the controller
            boolean success = controller.createOrUpdateWeeklyOpeningHours(wh);
            
            // Determine the appropriate success message based on existing records
            String message = controller.isDayExisting(wh.getDay()) 
                                ? "Day updated successfully" 
                                : "Day created successfully";

            // Prepare the server response
            ServerResponse response = new ServerResponse(success, wh, message);
            
            // Send the response wrapped in a Message object
            client.sendToClient(new Message(ActionType.CREATE_OPENING_HOURS, response));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Handle potential errors and notify the client of the failure
                ServerResponse response = new ServerResponse(false, null, "Error creating opening hours");
                client.sendToClient(new Message(ActionType.CREATE_OPENING_HOURS, response));
            } catch (IOException ignored) {}
        }
    }
}