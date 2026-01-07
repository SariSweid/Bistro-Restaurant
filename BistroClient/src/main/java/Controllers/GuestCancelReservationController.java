package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import Entities.Reservation;
import enums.ReservationStatus;
import handlers.ClientHandler;
import util.SceneManager;

public class GuestCancelReservationController {

    @FXML
    private TextField confirmationCodeField;
    
    

    // guestId of this guest
    private int guestId = -1;

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

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

        // SEND CANCEL REQUEST TO SERVER
        guestId = ClientHandler.getClient().getCurrentUserId();
        ClientHandler.getClient().cancelReservation( null, code, guestId);
    }

    @FXML
	public void onClose() {
        // close popup
        Stage stage = (Stage) confirmationCodeField.getScene().getWindow();
        stage.close();
    }

    public void showError(String message) {
        // simple alert
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showMessage(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
