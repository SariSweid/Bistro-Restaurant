package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

import Entities.Reservation;
import handlers.ClientHandler;

/**
 * Controller for allowing a guest to cancel their reservation using a confirmation code.
 * Extends {@link BaseDisplayController} to integrate with the reservation system.
 */
public class GuestCancelReservationController extends BaseDisplayController {

    @FXML
    private TextField confirmationCodeField;

    /**
     * The guest ID of the current guest.
     * Default is -1 until set.
     */
    private int guestId = -1;

    /**
     * Sets the guest ID for this controller.
     *
     * @param guestId the ID of the guest
     */
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    /**
     * Handles the submission of a cancellation request.
     * Validates the confirmation code entered by the guest and sends the cancel request to the server.
     */
    @FXML
    private void onSubmitCancel() {
        String input = confirmationCodeField.getText().trim();
        if (input.isBlank()) {
            showError("Please enter your confirmation code.");
            return;
        }

        int code;
        try {
            code = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showError("Invalid code.");
            return;
        }

        // Send cancel request to server
        guestId = ClientHandler.getClient().getCurrentUserId();
        ClientHandler.getClient().cancelReservation(null, code, guestId);
    }

    /**
     * Closes the current popup window.
     */
    @FXML
    public void onClose() {
        Stage stage = (Stage) confirmationCodeField.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an error message in a simple alert dialog.
     *
     * @param message the error message to show
     */
    public void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an informational message in a simple alert dialog.
     *
     * @param message the message to show
     */
    public void showMessage(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a list of reservations.
     * Not implemented for guest cancellation.
     *
     * @param list the list of {@link Reservation} objects
     */
    @Override
    public void showReservations(List<Reservation> list) {
        // Not used for guest cancel functionality
    }
}
