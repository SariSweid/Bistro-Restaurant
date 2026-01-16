package handlers;

import Controllers.BaseDisplayController;
import Controllers.CancelReservationController;
import Controllers.GuestCancelReservationController;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

public class CancelReservationHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        ServerResponse res = (ServerResponse) data;

        Platform.runLater(() -> {
            BaseDisplayController controller = ClientHandler.getClient().getActiveDisplayController();
            if (controller == null) return;

            // --- NOTIFICATION LOGIC FOR GUESTS ---
            // Check if the data contains a Reservation object that needs a notification
            if (res.getData() instanceof Entities.Reservation reservation) {
                if (reservation.getStatus() == enums.ReservationStatus.CANCELLED && !reservation.isNotified()) {
                    showGuestNotification(reservation);
                    // If we are in the guest screen, close the "Cancel" attempt
                    if (controller instanceof GuestCancelReservationController guestController) {
                        guestController.onClose();
                    }
                    return; 
                }
            }


            if (controller instanceof CancelReservationController subController) {
                if (res.isSuccess()) {
                    subController.refreshReservations();
                    SceneManager.showInfo(res.getMessage());
                } else {
                    SceneManager.showError(res.getMessage());
                }
            } else if (controller instanceof GuestCancelReservationController guestController) {
                if (res.isSuccess()) {
                    guestController.onClose();
                    guestController.showMessage(res.getMessage());
                } else {
                    guestController.showError(res.getMessage() + "\nThere is no reservation with that confirmation code.");
                }
            }
        });
    }

    private void showGuestNotification(Entities.Reservation res) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Reservation Already Cancelled");
        alert.setHeaderText("Important Update");
        alert.setContentText("Your reservation for " + res.getReservationDate() + 
                             " was already cancelled by the restaurant due to schedule updates.\n" +
                             "A notification was sent to your email.");
        alert.showAndWait();
        
        // Clear the flag so they don't get the popup again
        ClientHandler.getClient().markReservationAsNotified(res.getReservationID());
    }
}