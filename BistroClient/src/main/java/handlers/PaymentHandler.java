package handlers;

import Entities.Bill;
import common.ServerResponse;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class PaymentHandler implements ResponseHandler {

    private final Label statusLabel;
    private final Label amountLabel;

    public PaymentHandler(Label statusLabel, Label amountLabel) {
        this.statusLabel = statusLabel;
        this.amountLabel = amountLabel;
    }

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
                statusLabel.setStyle("-fx-text-fill: red;");//
            }
        });
    }
}
