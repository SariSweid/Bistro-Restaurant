package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import util.SceneManager;

public class RegisterHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) {
            System.out.println("RegisterHandler: Invalid data received");
            return;
        }

        
        // Must run UI updates on JavaFX thread
        Platform.runLater(() -> {
            Alert alert = new Alert(response.isSuccess() ?
                    Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);

            alert.setTitle("Registration");
            alert.setHeaderText(null);
            alert.setContentText(response.getMessage());
            alert.showAndWait();

            if (response.isSuccess()) {
                // After successful registration, go back to Supervisor UI
                SceneManager.switchTo("SupervisorUI.fxml");
            }
        });
    }
}