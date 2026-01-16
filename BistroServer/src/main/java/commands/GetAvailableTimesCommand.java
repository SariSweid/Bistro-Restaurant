package commands;

import logicControllers.ReservationController;
import messages.GetAvailableTimesRequest;
import server.Command;
import common.*;
import enums.ActionType;
import src.ocsf.server.ConnectionToClient;

import java.time.LocalTime;
import java.util.List;

/**
 * Command implementation for retrieving available time slots for a specific date and party size.
 * This class handles both standard reservation availability and waiting list time queries 
 * by delegating to the ReservationController.
 */
public class GetAvailableTimesCommand implements Command {

    /**
     * Controller that manages the logic for checking table availability and schedule constraints.
     */
    private final ReservationController controller = new ReservationController();

    /**
     * Executes the availability check. 
     * Processes a GetAvailableTimesRequest to determine if the user is looking for 
     * immediate openings or waiting list slots, then returns a ServerResponse 
     * containing a list of LocalTime objects.
     *
     * @param data    the GetAvailableTimesRequest containing the date, number of guests, and waiting list flag
     * @param client  the connection instance to the client that requested the information
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            GetAvailableTimesRequest req = (GetAvailableTimesRequest) data;

            List<LocalTime> times;

            // Determine which logic path to take based on the request type
            if (req.isForWaitingList()) {
                // Use the waiting list version
                times = controller.getAllTimesForWaitingList(req.getDate(), req.getGuests());
            } else {
                // Normal reservation
                times = controller.getAvailableTimes(req.getDate(), req.getGuests());
            }

            // Wrap the list in a ServerResponse and send it back
            client.sendToClient(
                new Message(
                    ActionType.GET_AVAILABLE_TIMES,
                    new ServerResponse(true, times, "Available times")
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
            try {
                // In case of an error, inform the client that a server error occurred
                client.sendToClient(
                    new Message(
                        ActionType.GET_AVAILABLE_TIMES,
                        new ServerResponse(false, null, "Server error")
                    )
                );
            } catch (Exception ignored) {}
        }
    }

}