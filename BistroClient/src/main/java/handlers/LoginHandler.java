package handlers;

import Entities.User;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

/**
 * Handles server responses related to the user login process.
 * This handler validates the received response, updates the current user
 * on successful login, and navigates the application to the appropriate
 * user interface based on the user's role.
 * In case of a failed login, an error message is displayed to the user.
 */
public class LoginHandler implements ResponseHandler {

    /**
     * Processes the server response received after a login attempt.
     * If the response indicates success, the current user is set in the
     * {@link ClientHandler}, and the application switches to the relevant
     * UI screen according to the user's role.
     * If the response indicates failure, an error message is shown.
     * All UI-related operations are executed on the JavaFX Application Thread.
     *
     * @param data the response object received from the server, expected
     *             to be of type {@link ServerResponse}
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) {

            System.out.println("LoginHandler: Invalid data received");
            return;
        }

        if (response.isSuccess()) {
            User user = (User) response.getData();
            ClientHandler.getClient().setCurrentUser(user);

            System.out.println("Login successful for " + user.getRole());

            Platform.runLater(() -> {
                switch (user.getRole()) {
                    case SUBSCRIBER -> SceneManager.switchTo("SubscriberUI.fxml");
                    case SUPERVISOR -> SceneManager.switchTo("SupervisorUI.fxml");
                    case MANAGER -> SceneManager.switchTo("ManagerUI.fxml");
                    default -> throw new IllegalArgumentException("Unexpected value: " + user.getRole());
                }
            });

        } else {
            Platform.runLater(() -> {
                SceneManager.showError(response.getMessage());
            });
        }
    }
}
