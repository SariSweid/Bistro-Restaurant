package Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import messages.PaymentRequest;
import util.SceneManager;

import java.time.LocalTime;
import java.util.Optional;

import handlers.ClientHandler;
import handlers.PaymentHandler;
import common.ServerResponse;
import enums.ActionType;

/**
 * Controller for handling payments in the UI.
 * Allows users to enter a confirmation code and process payment.
 * Displays the payment status and amount.
 */
public class PaymentController {

    /**
     * Label displaying the payment amount.
     */
    @FXML
    private Label amountLabel;

    /**
     * Label displaying the payment status messages.
     */
    @FXML
    private Label statusLabel;

    /**
     * Initializes the controller.
     * Sets the PaymentHandler for handling payment responses from the server.
     */
    @FXML
    public void initialize() {
        ClientHandler.getClient()
            .setHandler(ActionType.PAY, new PaymentHandler(statusLabel, amountLabel));
    }

    /**
     * Handles the payment process.
     * Prompts the user for a confirmation code, sends a PaymentRequest,
     * and updates the statusLabel with progress or errors.
     */
    @FXML
    private void onPay() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Payment Confirmation");
        dialog.setHeaderText("Confirmation Code Required");
        dialog.setContentText("Please enter the code:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                int confirmationCode = Integer.parseInt(result.get());
                
                LocalTime departureTime = LocalTime.now();
                
                PaymentRequest request = new PaymentRequest(confirmationCode);
                request.setDepartureTime(departureTime);

                statusLabel.setText("Processing payment...");
                statusLabel.setStyle("-fx-text-fill: black;");

                ClientHandler.getClient().Pay(request);

            } catch (NumberFormatException e) {
                statusLabel.setText("Error: Please enter a valid number");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    /**
     * Handles the response from the server after a payment request.
     * Updates the statusLabel with success or error messages on the JavaFX thread.
     *
     * @param response the ServerResponse containing success status and message
     */
    public void handlePaymentResponse(ServerResponse response) {
        Platform.runLater(() -> {
            if (response.isSuccess()) {
                statusLabel.setText("Payment processed successfully");
                statusLabel.setStyle("-fx-text-fill: green;");
            } else {
                statusLabel.setText(response.getMessage());
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });
    }

    /**
     * Navigates back to the main menu UI.
     */
    @FXML
    private void onPreviousPage() {
        switch (ClientHandler.getClient().getCurrentUserRole()) {
            case SUBSCRIBER -> SceneManager.switchTo("SubscriberUI.fxml");

            default -> SceneManager.switchTo("MainMenuUI.fxml");
        }
    }
}
