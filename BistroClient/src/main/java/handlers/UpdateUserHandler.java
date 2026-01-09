package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class UpdateUserHandler implements ResponseHandler {

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
