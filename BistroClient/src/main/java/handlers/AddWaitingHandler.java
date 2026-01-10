package handlers;

import Controllers.BaseDisplayController;
import Controllers.GuestWaitingListController;
import Controllers.SubscriberWaitingListController;
import Entities.WaitingListEntry;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class AddWaitingHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        Platform.runLater(() -> {
            BaseDisplayController controller = ClientHandler.getClient().getActiveDisplayController();
            if (controller == null) return;

            if (data instanceof ServerResponse res) {
                if (res.isSuccess()) {
                 
                    if (res.getData() instanceof WaitingListEntry entry) {
                        int code = entry.getConfirmationCode();

                        if (controller instanceof GuestWaitingListController guestController) {
                            guestController.clearAddFields();
                            SceneManager.showInfo("Reservation confirmed! Confirmation code: " + code);
                        } else if (controller instanceof SubscriberWaitingListController subController) {
                            subController.clearAddFields();
                            SceneManager.showInfo("Reservation confirmed! Confirmation code: " + code);
                        }
                    }
                } else {
                    SceneManager.showError("Failed to add to waiting list: " + res.getMessage());
                }
            } else {
                SceneManager.showError("Unexpected server response for Add Waiting List.");
            }
        });
    }
}
