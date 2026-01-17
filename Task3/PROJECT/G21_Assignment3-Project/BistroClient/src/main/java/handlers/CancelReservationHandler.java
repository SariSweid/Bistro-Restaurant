package handlers;

import Controllers.BaseDisplayController;
import Controllers.CancelReservationController;
import Controllers.GuestCancelReservationController;
import common.ServerResponse;
import javafx.application.Platform;
import util.SceneManager;

/**
 * Handles server responses related to reservation cancellations.
 * Processes both subscriber and guest cancellation responses, 
 * updates the UI accordingly, and displays notifications if needed.
 */
public class CancelReservationHandler implements ResponseHandler {

    /**
     * Handles the server response for a reservation cancellation.
     * Updates the active display controller on the JavaFX Application Thread.
     * For guests, it may show a notification if the reservation was already cancelled.
     * For subscribers or guests, it updates the UI based on success or failure.
     *
     * @param data the server response object, expected to be of type ServerResponse
     */
    @Override
    public void handle(Object data) {
        ServerResponse res = (ServerResponse) data;

        Platform.runLater(() -> {
            BaseDisplayController controller = ClientHandler.getClient().getActiveDisplayController();
            if (controller == null) return;

            // --- NOTIFICATION LOGIC FOR GUESTS ---
            if (res.getData() instanceof Entities.Reservation reservation) {
                if (reservation.getStatus() == enums.ReservationStatus.CANCELLED && !reservation.isNotified()) {
                    showGuestNotification(reservation);
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

    /**
     * Shows a notification to the guest if the reservation was already cancelled
     * by the restaurant due to schedule updates.
     * Marks the reservation as notified to prevent duplicate alerts.
     *
     * @param res the reservation object that was already cancelled
     */
    private void showGuestNotification(Entities.Reservation res) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Reservation Already Cancelled");
        alert.setHeaderText("Important Update");
        alert.setContentText("Your reservation for " + res.getReservationDate() + 
                             " was already cancelled by the restaurant due to schedule updates.\n" +
                             "A notification was sent to your email.");
        alert.showAndWait();

        ClientHandler.getClient().markReservationAsNotified(res.getReservationID());
    }
}
