package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class CreateOpeningHoursHandler implements ResponseHandler {
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

