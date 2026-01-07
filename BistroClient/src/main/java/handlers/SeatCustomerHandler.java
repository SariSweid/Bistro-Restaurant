package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class SeatCustomerHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) {
            System.out.println("SeatCustomerHandler: Invalid data received");
            return;
        }

        Platform.runLater(() -> {
            if (response.isSuccess()) {
                Integer table = (Integer) response.getData();
                SceneManager.showInfo(response.getMessage() + " Table #" + table);
            } else {
                SceneManager.showError(response.getMessage());
            }
        });
    }
}
