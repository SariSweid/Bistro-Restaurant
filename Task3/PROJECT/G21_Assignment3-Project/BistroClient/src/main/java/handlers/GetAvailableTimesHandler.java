package handlers;

import java.time.LocalTime;
import java.util.List;

import Controllers.AvailableTimesListener;
import common.ServerResponse;
import javafx.application.Platform;

/**
 * Handles the server response for available reservation times.
 * 
 * Extracts the list of available LocalTime slots from the server response
 * and notifies the currently active AvailableTimesListener on the JavaFX
 * application thread.
 */
public class GetAvailableTimesHandler implements ResponseHandler {

    /**
     * Processes the server response containing available reservation times.
     * 
     * If the response is successful and a listener is registered, the listener
     * is updated with the received list of available times.
     *
     * @param data the response object received from the server; expected to be a ServerResponse
     */
    @Override
    public void handle(Object data) {

        if (!(data instanceof ServerResponse response)) {
            System.out.println("Unexpected server response for GET_AVAILABLE_TIMES");
            return;
        }

        if (!response.isSuccess()) {
            System.out.println(response.getMessage());
            return;
        }

        @SuppressWarnings("unchecked")
        List<LocalTime> times = (List<LocalTime>) response.getData();

        AvailableTimesListener listener =
                ClientHandler.getClient().getAvailableTimesListener();

        if (listener == null) {
            System.out.println("No AvailableTimesListener is currently active");
            return;
        }

        Platform.runLater(() -> listener.updateAvailableTimes(times));
    }
}
