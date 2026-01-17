package handlers;

import Controllers.BaseDisplayController;
import Controllers.SubscriberWaitingListController;
import Controllers.GuestWaitingListController;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

/**
 * Handles server responses related to cancellation of waiting list entries.
 * Updates the UI of the active display controller on the JavaFX Application Thread.
 * Clears the confirmation code field for subscribers or guests
 * and shows an informational or error message based on the server response.
 */
public class CancelWaitingHandler implements ResponseHandler {

    /**
     * Processes the server response for a waiting list cancellation request.
     * If the cancellation is successful, clears the confirmation code field
     * in the active controller and shows an information alert.
     * If the cancellation fails, shows an error alert.
     *
     * @param data the server response object, expected to be of type ServerResponse
     */
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
