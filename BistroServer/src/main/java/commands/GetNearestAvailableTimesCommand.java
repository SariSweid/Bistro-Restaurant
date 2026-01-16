package commands;

import java.util.List;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.ReservationController;
import messages.AvailableDateTimes;
import messages.GetNearestAvailableTimesRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for retrieving the nearest available reservation time slots.
 * This class processes requests to find alternative dates and times when a specific 
 * requested slot might be unavailable or as part of a general search.
 */
public class GetNearestAvailableTimesCommand implements Command {

    /**
     * Controller handling the business logic for reservations and availability calculations.
     */
    private final ReservationController controller = new ReservationController();

    /**
     * Executes the logic to find nearest available times.
     * Extracts the date and guest count from the request, queries the reservation 
     * controller, and returns a response containing a list of available slots.
     *
     * @param data    the GetNearestAvailableTimesRequest containing the desired date and guests
     * @param client  the connection to the client that requested the search
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {

        try {
            GetNearestAvailableTimesRequest req =
                (GetNearestAvailableTimesRequest) data;

            // Retrieve the list of available slots from the logic controller
            List<AvailableDateTimes> result =
                controller.getNearestAvailableDates(
                    req.getDate(),
                    req.getGuests()
                );

            // Send success response with the result list
            client.sendToClient(
                new Message(
                    ActionType.GET_NEAREST_TIMES,
                    new ServerResponse(true, result, "Nearest available times")
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Send failure response in case of an exception
                client.sendToClient(
                    new Message(
                        ActionType.GET_NEAREST_TIMES,
                        new ServerResponse(false, null, "Server error")
                    )
                );
            } catch (Exception ignored) {}
        }
    }
}