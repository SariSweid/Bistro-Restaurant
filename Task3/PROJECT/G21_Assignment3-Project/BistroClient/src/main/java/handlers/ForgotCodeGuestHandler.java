package handlers;

import javafx.application.Platform;
import util.SceneManager;

/**
 * Handler for processing the server response when a guest requests their forgotten confirmation code.
 * Displays an informational message indicating that the code was sent to the guest's email or phone.
 */
public class ForgotCodeGuestHandler implements ResponseHandler {

    /**
     * Handles the server response for a forgotten confirmation code request.
     * Always shows an informational message on the UI thread.
     *
     * @param data the response data from the server (not used in this handler)
     */
    @Override
    public void handle(Object data) {
        Platform.runLater(() -> {
            SceneManager.showInfo("Sent to your email or phone");
        });
    }
}
