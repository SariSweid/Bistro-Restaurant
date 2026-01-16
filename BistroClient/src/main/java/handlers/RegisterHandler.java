package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import util.SceneManager;

/**
 * Handles server responses related to user registration.
 * This handler validates the received response and shows an alert
 * to indicate whether the registration was successful or failed.
 * If the registered user is not a guest and the registration succeeds,
 * the application navigates to the Supervisor UI.
 */
public class RegisterHandler implements ResponseHandler {

    /**
     * Processes the server response received after a registration attempt.
     * If the response contains a user with role GUEST, no action is taken.
     * Otherwise, an alert is displayed to show the result of the registration.
     * If registration is successful, the application switches to the
     * Supervisor UI.
     *
     * @param data the response object received from the server, expected
     *             to be of type {@link ServerResponse}
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) {
            System.out.println("RegisterHandler: Invalid data received");
            return;
        }

        Object obj = response.getData();
        if (obj instanceof Entities.User user) {
            if (user.getRole() == enums.UserRole.GUEST) {
                return;
            }
        }

        Platform.runLater(() -> {
            Alert alert = new Alert(response.isSuccess() ?
                    Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);

            alert.setTitle("Registration");
            alert.setHeaderText(null);
            alert.setContentText(response.getMessage());
            alert.showAndWait();

            if (response.isSuccess()) {
                SceneManager.switchTo("SupervisorUI.fxml");
            }
        });
    }
}
