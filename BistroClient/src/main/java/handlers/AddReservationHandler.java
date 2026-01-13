package handlers;

import java.io.IOException;
import java.util.List;

import Controllers.BaseReservationController;
import Controllers.GuestMakeReservationController;
import Entities.Reservation;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class AddReservationHandler implements ResponseHandler {
	
    @Override
    public void handle(Object data) {
    	
    		BaseReservationController controller;
			controller = ClientHandler.getClient().getActiveReservationController();

        if (controller == null) {
            System.out.println("ERROR: GuestMakeReservationController is null");
            return;
        }

        Platform.runLater(() -> {
            if (data instanceof ServerResponse res) {
                if (res.isSuccess()) {
                    controller.showConfirmation("Reservation added successfully!\nConfirmation code: " +
                            ((res.getData() instanceof Entities.Reservation r) ? r.getConfirmationCode() : "N/A"));
                    

                    // Refresh OrderController table if it exists
                    var displayController = ClientHandler.getClient().getActiveDisplayController();
                    if (displayController != null) {
                        displayController.showReservations(List.of((Reservation) res.getData()));
                    }
                } else {
                    controller.showError("Failed to add reservation: " + res.getMessage());
                }
            } else {
                controller.showError("Unexpected server response for Add Reservation.");
            }
        });
    }
}
