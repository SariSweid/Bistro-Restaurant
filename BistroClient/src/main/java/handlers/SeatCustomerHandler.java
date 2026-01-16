package handlers;

import Entities.Reservation;
import common.ServerResponse;
import enums.ReservationStatus;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import util.SceneManager;

public class SeatCustomerHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) {
            System.out.println("SeatCustomerHandler: Invalid data received");
            return;
        }

        Platform.runLater(() -> {
        	
        		// --- GUEST NOTIFICATION CHECK ---
        		// If the data returned is a Reservation object, check if it's a silent cancellation
            if (response.getData() instanceof Reservation res) {
                if (res.getStatus() == ReservationStatus.CANCELLED && !res.isNotified()) {
                    showGuestPopup(res);
                    return;
                }
            }
            
            if (response.isSuccess()) {
                Integer table = (Integer) response.getData();
                SceneManager.showInfo(response.getMessage() + " Table #" + table);
            } else {
                SceneManager.showError(response.getMessage());
            }
        });
    }
    
    // Helper method to show the notification
    private void showGuestPopup(Reservation res) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Reservation Status");
        alert.setHeaderText("Reservation Already Cancelled");
        alert.setContentText("We apologize, but your reservation for " + res.getReservationDate() + 
                             " was cancelled due to restaurant schedule changes.");
        alert.showAndWait();

        // Mark as notified so they don't see this again
        ClientHandler.getClient().markReservationAsNotified(res.getReservationID());
    }
}
