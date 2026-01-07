package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class ForgotCodeHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) {
            System.out.println("ForgotCodeHandler: Invalid data received");
            return;
        }

        Platform.runLater(() -> {
            if (response.isSuccess()) {
                SceneManager.showInfo("Confirmation code sent to your email/phone.");
            } else {
                SceneManager.showError("There is no reservations found.");
            }
        });
    }
}
