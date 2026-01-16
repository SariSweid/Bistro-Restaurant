package handlers;

import Entities.Reservation;
import common.ServerResponse;
import enums.ReservationStatus;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import util.SceneManager;

/**
 * Handles server responses related to seating a customer.
 * Updates the UI based on the success or failure of the seating operation.
 * Also handles silent cancellations for reservations by notifying the guest.
 */
public class SeatCustomerHandler implements ResponseHandler {

    /**
     * Processes the server response for seating a customer.
     * If the operation was successful, displays an informational message with the table number.
     * If the operation failed, displays an error message.
     * If the reservation was silently cancelled, shows a warning popup to the guest.
     * All UI updates are executed on the JavaFX application thread.
     *
     * @param data the server response object containing the result of the seating operation
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof ServerResponse response)) {
            System.out.println("SeatCustomerHandler: Invalid data received");
            return;
        }

        Platform.runLater(() -> {
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

    /**
     * Displays a popup to notify the guest about a reservation that was silently cancelled.
     * Marks the reservation as notified to prevent repeated notifications.
     *
     * @param res the reservation that was cancelled
     */
    private void showGuestPopup(Reservation res) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Reservation Status");
        alert.setHeaderText("Reservation Already Cancelled");
        alert.setContentText("We apologize, but your reservation for " + res.getReservationDate() + 
                             " was cancelled due to restaurant schedule changes.");
        alert.showAndWait();

        ClientHandler.getClient().markReservationAsNotified(res.getReservationID());
    }
}

