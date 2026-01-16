package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

/**
 * ResponseHandler implementation for handling the server response
 * when creating a new opening day in the system.
 * Shows an information alert on success or an error alert on failure.
 */
public class CreateOpeningHoursHandler implements ResponseHandler {

    /**
     * Handles the server response for creating a new opening day.
     * If the response indicates success, shows an information alert.
     * If the response indicates failure, shows an error alert with the server message.
     *
     * @param data the server response object, expected to be a {@link ServerResponse}
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) return;

        Platform.runLater(() -> {
            if (response.isSuccess()) {
                SceneManager.showInfo("Day created successfully!");
            } else {
                SceneManager.showError("Failed to create day: " + response.getMessage());
            }
        });
    }
}
