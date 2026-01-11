package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class DeleteOpeningHoursHandler implements ResponseHandler {

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
