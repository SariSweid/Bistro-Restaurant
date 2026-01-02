package handlers;

import common.ServerResponse;
import enums.ActionType;
import javafx.application.Platform;
import Entities.User;
import client.GuestReservationUI;
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
            System.out.println("Login successful for " + user.getRole());

            // Switch to UI on FX Application Thread
            Platform.runLater(() -> {
                switch (user.getRole()) {
                    case SUBSCRIBER -> SceneManager.switchTo("SubscriberUI.fxml");
                    case SUPERVISOR -> SceneManager.switchTo("SupervisorUI.fxml");
                    case MANAGER -> SceneManager.switchTo("ManagerUI.fxml");
                }
            });

        } else {
            System.out.println("Login failed: " + response.getMessage());
            // Optionally show error to the user in UI
        }
    }
}
