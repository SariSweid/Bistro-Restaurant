package handlers;

import java.util.List;

import Controllers.BaseReservationController;
import common.ServerResponse;
import javafx.application.Platform;
import messages.AvailableDateTimes;

/**
 * Handler for processing the server response that contains the nearest available
 * reservation date and time options.
 * Passes the available options to the active reservation controller.
 */
public class GetNearestAvailableTimesHandler implements ResponseHandler {

    /**
     * Handles the server response for retrieving nearest available reservation times.
     * Validates the response, extracts the available date-time options,
     * and updates the active reservation controller on the JavaFX thread.
     *
     * @param data the response data from the server, expected to be a ServerResponse
     *             containing a list of AvailableDateTimes
     */
    @Override
    public void handle(Object data) {

        if (!(data instanceof ServerResponse res)) {
            System.out.println("Unexpected response for nearest times");
            return;
        }

        if (!res.isSuccess()) {
            System.out.println("Nearest times failed: " + res.getMessage());
            return;
        }

        @SuppressWarnings("unchecked")
        List<AvailableDateTimes> options = (List<AvailableDateTimes>) res.getData();

        BaseReservationController controller =
                ClientHandler.getClient().getActiveReservationController();

        if (controller == null) return;

        Platform.runLater(() ->
            controller.showNearestAvailableTimes(options)
        );
    }
}
