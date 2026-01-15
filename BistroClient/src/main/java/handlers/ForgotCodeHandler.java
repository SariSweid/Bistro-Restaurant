package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;
import java.util.List;

public class ForgotCodeHandler implements ResponseHandler {

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