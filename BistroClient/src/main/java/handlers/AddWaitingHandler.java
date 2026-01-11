package handlers;

import Controllers.BaseDisplayController;
import Controllers.GuestWaitingListController;
import Controllers.SubscriberWaitingListController;
import Entities.Reservation;
import Entities.WaitingListEntry;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class AddWaitingHandler implements ResponseHandler {

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
	            SceneManager.showError("Operation failed: " + res.getMessage());
	            return;
	        }

	        // Handle Reservation (table assigned immediately)
	        if (res.getData() instanceof Reservation reservation) {
	            SceneManager.showInfo("Table available! please procceed");
	            if (controller instanceof GuestWaitingListController guestController) {
	                guestController.clearAddFields();
	            } else if (controller instanceof SubscriberWaitingListController subController) {
	                subController.clearAddFields();
	            }
	        }
	        // Handle WaitingListEntry (no table available)
	        else if (res.getData() instanceof WaitingListEntry entry) {
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


