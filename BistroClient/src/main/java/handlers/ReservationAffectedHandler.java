package handlers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.SceneManager;

public class ReservationAffectedHandler implements ResponseHandler {
    @Override
    public void handle(Object data) {
        String msg = (String) data;
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Table Conflict");
            alert.setHeaderText("Reservations Affected");
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }
}