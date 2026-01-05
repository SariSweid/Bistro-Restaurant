package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog; // חובה לייבא
import messages.PaymentRequest;
import util.SceneManager;
import java.util.Optional;

import handlers.ClientHandler;

public class PaymentController {

    @FXML
    private Label statusLabel;

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

                // שליחה לשרת
                PaymentRequest request = new PaymentRequest(confirmationCode);
                ClientHandler.getClient().Pay(request);

                statusLabel.setText("Payment request sent");
                statusLabel.setStyle("-fx-text-fill: green;");

            } catch (NumberFormatException e) {
                statusLabel.setText("Error: Please enter a valid number");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    


    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }
}