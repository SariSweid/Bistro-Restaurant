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
            BaseDisplayController controller =
                    ClientHandler.getClient().getActiveDisplayController();
            if (controller == null) return;

            if (res.isSuccess()) {

                if (controller instanceof SubscriberWaitingListController sub) {
                    sub.clearConfirmationCodeField();
                } else if (controller instanceof GuestWaitingListController guest) {
                    guest.clearConfirmationCodeField();
                }

                SceneManager.showInfo(res.getMessage());

            } else {
                SceneManager.showError(res.getMessage());
            }
        });
    }
}
