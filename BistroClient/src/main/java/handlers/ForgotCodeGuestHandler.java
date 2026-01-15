package handlers;

import javafx.application.Platform;
import util.SceneManager;

public class ForgotCodeGuestHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        Platform.runLater(() -> {
            SceneManager.showInfo("Sent to your email or phone");
        });
    }
}
