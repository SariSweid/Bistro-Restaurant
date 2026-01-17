package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

/**
 * Handler for processing the server response after attempting to delete weekly opening hours.
 * Displays a success or error message on the UI depending on the response.
 */
public class DeleteOpeningHoursHandler implements ResponseHandler {

    /**
     * Handles the server response for deleting opening hours.
     * If successful, shows an informational message.
     * If failed, shows an error message with the server-provided reason.
     *
     * @param data the response data from the server, expected to be a ServerResponse object
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) return;

        Platform.runLater(() -> {
            if (response.isSuccess()) {
                SceneManager.showInfo("Day deleted successfully!");
            } else {
                SceneManager.showError("Failed to delete day: " + response.getMessage());
            }
        });
    }
}
