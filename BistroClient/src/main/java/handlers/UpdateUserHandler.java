package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

/**
 * Handles server responses for user profile updates.
 * 
 * This handler checks whether the server response indicates success or failure
 * and updates the UI accordingly. All UI updates are executed on the
 * JavaFX Application Thread.
 */
public class UpdateUserHandler implements ResponseHandler {

    /**
     * Processes the server response for a user update request.
     * 
     * If the response indicates success, an informational message is displayed.
     * If the response indicates failure, an error message with the server's
     * message is shown.
     *
     * @param data the server response object, expected to be an instance of ServerResponse
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse res)) return;

        Platform.runLater(() -> {
            if (res.isSuccess()) {
                SceneManager.showInfo("Profile updated successfully");
            } else {
                SceneManager.showError(res.getMessage());
            }
        });
    }
}
