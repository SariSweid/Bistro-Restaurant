package handlers;

import Controllers.BaseReservationController;
import Controllers.GuestMakeReservationController;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class AddReservationHandler implements ResponseHandler {
	
    @Override
    public void handle(Object data) {
    	
    		BaseReservationController controller = ClientHandler.getClient().getActiveReservationController();

        if (controller == null) {
            System.out.println("ERROR: GuestMakeReservationController is null");
            return;
        }

        Platform.runLater(() -> {
            if (data instanceof ServerResponse res) {
                if (res.isSuccess()) {
                    controller.showConfirmation("Reservation added successfully!\nConfirmation code: " +
                            ((res.getData() instanceof Entities.Reservation r) ? r.getConfirmationCode() : "N/A"));
                } else {
                    controller.showError("Failed to add reservation: " + res.getMessage());
                }
            } else {
                controller.showError("Unexpected server response for Add Reservation.");
            }
        });
    }
}
