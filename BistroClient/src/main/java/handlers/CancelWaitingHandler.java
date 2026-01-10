package handlers;

import Controllers.BaseDisplayController;
import Controllers.SubscriberWaitingListController;
import Controllers.GuestWaitingListController;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class CancelWaitingHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        ServerResponse res = (ServerResponse) data;

        Platform.runLater(() -> {
            BaseDisplayController controller = ClientHandler.getClient().getActiveDisplayController();
            if (controller == null) return;

            if (controller instanceof SubscriberWaitingListController) {
                SubscriberWaitingListController waitingController = (SubscriberWaitingListController) controller;

                if (res.isSuccess()) {
                    waitingController.clearConfirmationCodeField();
                    SceneManager.showInfo(res.getMessage());
                } else {
                    SceneManager.showError(res.getMessage() + "\nThere is no waiting list entry with that confirmation code.");
                }

            } else if (controller instanceof GuestWaitingListController) {
                GuestWaitingListController guestController = (GuestWaitingListController) controller;

                if (res.isSuccess()) {
                    guestController.clearConfirmationCodeField();
                    SceneManager.showInfo(res.getMessage());
                } else {
                    SceneManager.showError(res.getMessage() + "\nThere is no waiting list entry with that confirmation code.");
                }
            }
        });
    }
}
