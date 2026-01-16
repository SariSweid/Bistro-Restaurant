package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.SceneManager;
import java.time.LocalTime;
import handlers.ClientHandler;

public class TableReceivingGuestController {

    @FXML
    private TextField confirmationCode;

    @FXML
    private void onPreviousPage() {

            SceneManager.switchTo("MainMenuUI.fxml");
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
            
            // Send current time with seat request
            LocalTime actualArrival = LocalTime.now();
            
            ClientHandler.getClient().seatCustomer(userId, code, actualArrival);

        } catch (NumberFormatException e) {
            SceneManager.showError("Confirmation code must be a number.");
        }
    }


    /**
     * User clicks "forgot my code"
     */
    @FXML
    private void onForgot() {

            SceneManager.showInfo("Sent to your email or phone");

    }

}

