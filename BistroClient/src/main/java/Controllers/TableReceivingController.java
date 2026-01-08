package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import messages.ForgotCodeRequest;
//import messages.SeatCustomerRequest;
import util.SceneManager;
import enums.UserRole;
import handlers.ClientHandler;

public class TableReceivingController {

    @FXML
    private TextField confirmationCode;

    @FXML
    private void onPreviousPage() {
        int userId = ClientHandler.getClient().getCurrentUserId();

        enums.UserRole role = ClientHandler.getClient().getCurrentUserRole();

        if (role == enums.UserRole.SUBSCRIBER) {
            // Subscriber goes back to Subscriber UI
            util.SceneManager.switchTo("SubscriberUI.fxml");
        } else {
            // Guest goes back to MainMenu UI
            util.SceneManager.switchTo("MainMenuUI.fxml");
        }

    }

    /**
     * User enters confirmation code and clicks ENTER
     */
    @FXML
    private void onEnter() {

        String text = confirmationCode.getText();

        if (text == null || text.isEmpty()) {
            SceneManager.showError("Please enter your confirmation code.");
            return;
        }

        int userId = ClientHandler.getClient().getCurrentUserId();

        try {
            int code = Integer.parseInt(text);
            ClientHandler.getClient().seatCustomer(userId, code);

        } catch (NumberFormatException e) {
            SceneManager.showError("Confirmation code must be a number.");
        }
    }


    /**
     * User clicks "forgot my code"
     */
    @FXML
    private void onForgot() {

        int userId = ClientHandler.getClient().getCurrentUserId();

        // Send request to server
        ClientHandler.getClient().forgotCode(userId);
    }
}
