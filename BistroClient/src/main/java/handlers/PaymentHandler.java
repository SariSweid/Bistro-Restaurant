package handlers;

import Entities.Bill;
import common.ServerResponse;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Handles server responses related to payment processing.
 * Updates the UI labels for payment status and final amount
 * on the JavaFX application thread.
 */
public class PaymentHandler implements ResponseHandler {

    private final Label statusLabel;
    private final Label amountLabel;

    /**
     * Constructs a PaymentHandler with labels to display the status
     * and amount of the payment.
     *
     * @param statusLabel the Label used to show payment status messages
     * @param amountLabel the Label used to display the final payment amount
     */
    public PaymentHandler(Label statusLabel, Label amountLabel) {
        this.statusLabel = statusLabel;
        this.amountLabel = amountLabel;
    }

    /**
     * Processes the server response for a payment request.
     * If the operation was successful, updates the status label in green
     * and displays the final bill amount.
     * If the operation failed, updates the status label in red with the error message.
     * All UI updates are run on the JavaFX application thread.
     *
     * @param data the server response object containing the result of the payment
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) return;

        Platform.runLater(() -> {
            if (response.isSuccess()) {
                Bill bill = (Bill) response.getData();

                statusLabel.setText(response.getMessage());
                statusLabel.setStyle("-fx-text-fill: green;");

                amountLabel.setText("Final Amount: " + bill.getTotalAmount());
            } else {
                statusLabel.setText(response.getMessage());
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });
    }
}

