package handlers;

import java.time.LocalTime;
import java.util.List;

import Controllers.AvailableTimesListener;
import common.ServerResponse;
import javafx.application.Platform;

/**
 * Handles server responses for available reservation times.
 * Notifies the currently active AvailableTimesListener.
 */
public class GetAvailableTimesHandler implements ResponseHandler {

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
