package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;
import java.util.List;

/**
 * ResponseHandler implementation for handling the server response
 * when a user requests their forgotten reservation confirmation code.
 * Displays an information alert if the code was sent successfully,
 * or an error alert if no reservations were found.
 */
public class ForgotCodeHandler implements ResponseHandler {

    /**
     * Handles the server response for a "forgot code" request.
     * If the response is successful and contains reservations, shows an info alert indicating
     * that the confirmation code has been sent to the user's email or phone.
     * If no reservations are found or the response indicates failure, shows an error alert.
     *
     * @param data the server response object, expected to be a {@link ServerResponse}
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) return;

        Platform.runLater(() -> {
            if (!response.isSuccess()) {
                SceneManager.showError("No reservations found");
                return;
            }

            Object respData = response.getData();

            if (respData instanceof List<?> reservations) {
                if (reservations.isEmpty()) {
                    SceneManager.showError("No reservations found");
                } else {
                    SceneManager.showInfo("Sent to your email or phone");
                }
            } else {
                SceneManager.showInfo("Sent to your email or phone");
            }
        });
    }
}
