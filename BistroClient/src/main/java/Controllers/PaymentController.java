package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import util.SceneManager;

public class PaymentController {

    @FXML
    private Label statusLabel;

    @FXML
    private void onPay() {
        statusLabel.setText("Payment completed successfully");
    }/*should do multiple checks tests*/

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberMenuUI.fxml");
    }
}

