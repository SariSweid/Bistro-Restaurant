package handlers;

import Entities.User;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class LoginHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) {
        	
            System.out.println("LoginHandler: Invalid data received");
            return;
        }

        if (response.isSuccess()) {
            User user = (User) response.getData();
            ClientHandler.getClient().setCurrentUserRole(user.getRole());
            System.out.println("Login successful for " + user.getRole());
            
            // store in client handler
            ClientHandler client = ClientHandler.getClient();
            client.setCurrentUserId(user.getUserId());
            client.setCurrentUserRole(user.getRole());

            // Switch to UI on FX Application Thread
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
