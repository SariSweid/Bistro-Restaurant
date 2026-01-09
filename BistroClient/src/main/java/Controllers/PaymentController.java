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

public class PaymentController  {
	


	@FXML
	private Label amountLabel;

    @FXML
    private Label statusLabel;
    
    @FXML
    public void initialize() {
        ClientHandler.getClient()
            .setHandler(ActionType.PAY, new PaymentHandler(statusLabel, amountLabel));
    }

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

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }
}
