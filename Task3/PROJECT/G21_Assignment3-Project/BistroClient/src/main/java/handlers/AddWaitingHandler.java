package handlers;

import Controllers.BaseDisplayController;
import Controllers.GuestWaitingListController;
import Controllers.SubscriberWaitingListController;
import Entities.Reservation;
import Entities.WaitingListEntry;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

/**
 * ResponseHandler implementation for handling the server response
 * after attempting to add a reservation or waiting list entry.
 * This handler updates the UI on the JavaFX Application thread, 
 * showing appropriate information or error alerts. 
 * It also clears input fields in the active waiting list controllers.
 */
public class AddWaitingHandler implements ResponseHandler {

    /**
     * Handles the server response for adding a reservation or waiting list entry.
     * If the response indicates success:
     *  - For a Reservation: shows a success message and clears input fields.
     *  - For a WaitingListEntry: shows the confirmation code and clears input fields.
     * If the response indicates failure or the data is invalid, shows an error alert.
     *
     * @param data the server response object, expected to be a ServerResponse
     */
    @Override
    public void handle(Object data) {
        Platform.runLater(() -> {

            BaseDisplayController controller = ClientHandler.getClient().getActiveDisplayController();
            if (controller == null) {
                SceneManager.showError("No active controller found.");
                return;
            }

            if (!(data instanceof ServerResponse res)) {
                SceneManager.showError("Unexpected server response.");
                return;
            }

            if (!res.isSuccess()) {
                SceneManager.showError(res.getMessage());
                return;
            }

            if (res.getData() instanceof Reservation _) {
                SceneManager.showInfo(res.getMessage());
                if (controller instanceof GuestWaitingListController guestController) {
                    guestController.clearAddFields();
                } else if (controller instanceof SubscriberWaitingListController subController) {
                    subController.clearAddFields();
                }
            } else if (res.getData() instanceof WaitingListEntry entry) {
                SceneManager.showInfo("Added to waiting list. Your confirmation code: " + entry.getConfirmationCode());
                if (controller instanceof GuestWaitingListController guestController) {
                    guestController.clearAddFields();
                } else if (controller instanceof SubscriberWaitingListController subController) {
                    subController.clearAddFields();
                }
            }
        });
    }
}
