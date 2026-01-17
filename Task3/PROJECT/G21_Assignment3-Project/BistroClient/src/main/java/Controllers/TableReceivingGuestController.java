package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.SceneManager;
import java.time.LocalTime;
import handlers.ClientHandler;

/**
 * Controller for the "Table Receiving" UI for guests.
 * Handles entering a confirmation code, seating the guest, and recovering forgotten codes.
 */
public class TableReceivingGuestController {

    /**
     * TextField for entering the guest's confirmation code.
     */
    @FXML
    private TextField confirmationCode;

    /**
     * Navigates back to the main menu UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    /**
     * Handles the "Enter" action when the user submits their confirmation code.
     * Validates the input and sends a seat request with the current time to the server.
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

            LocalTime actualArrival = LocalTime.now();

            ClientHandler.getClient().seatCustomer(userId, code, actualArrival);

        } catch (NumberFormatException e) {
            SceneManager.showError("Confirmation code must be a number.");
        }
    }

    /**
     * Handles the "Forgot my code" action.
     * Shows an informational message that the confirmation code was sent to the guest's email or phone.
     */
    @FXML
    private void onForgot() {
        SceneManager.showInfo("Sent to your email or phone");
    }
}
