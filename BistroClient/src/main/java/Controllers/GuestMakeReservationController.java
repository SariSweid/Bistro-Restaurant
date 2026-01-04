package Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import Entities.Reservation;
import enums.ReservationStatus;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import util.SceneManager;
import handlers.ClientHandler;

public class GuestMakeReservationController {

    @FXML
    private TextField NumberOfDiners, emailOrPhone;

    @FXML
    private DatePicker datePicker;
    
    @FXML
    private ComboBox<LocalTime> timeComboBox;

    @FXML
    private Button confirmButton;

    @FXML
    private Label timesLabel;

    // --- Previous Page ---
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("GuestReservationUI.fxml");
    }

    // --- Check Availability ---
    @FXML
    private void onCheckAvailability() {
    	
        int diners;
        LocalDate resDate = datePicker.getValue();
        
        if (resDate == null) {
            showError("Please select a valid date.");
            return;
        }
        
        try {
            diners = Integer.parseInt(NumberOfDiners.getText());
            if (diners <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            showError("Please enter a valid number of diners");
            return;
        }

        if (emailOrPhone.getText().isBlank()) {
            showError("Please enter your email or phone");
            return;
        }

        // Request available times from server
        ClientHandler.getClient().getAvailableTimes(resDate, diners);
    }

    // --- Called by GetAvailableTimesHandler after server returns times ---
    public void updateAvailableTimes(List<LocalTime> times) {
        Platform.runLater(() -> {
            if (times.isEmpty()) {
                showError("No available times. Try another day or fewer diners");
                return;
            }

            timeComboBox.getItems().clear();
            timeComboBox.getItems().addAll(times);

            // Show the ComboBox, label, and confirm button
            timeComboBox.setVisible(true);
            timeComboBox.setManaged(true);
            confirmButton.setVisible(true);
            confirmButton.setManaged(true);
            timesLabel.setVisible(true);
            timesLabel.setManaged(true);
        });
    }

    // --- Confirm Reservation ---
    @FXML
    private void onSubmitOrder() {
        LocalTime selectedTime = timeComboBox.getValue();
        if (selectedTime == null) {
            showError("Please select a time.");
            return;
        }

        int diners = Integer.parseInt(NumberOfDiners.getText());
        LocalDate resDate = datePicker.getValue();
        String contact = emailOrPhone.getText();

        // ReservationID = 0, confirmation code generated server-side
        Reservation r = new Reservation(	
                0,  // reservationID (server assigns)
                1,  // customerID for guest
                diners,
                0,  // confirmationCode (server generates)
                resDate,
                selectedTime,
                ReservationStatus.CONFIRMED
        );

        
        System.out.println("Sending reservation: " + r);
        // Send reservation to server
        ClientHandler.getClient().addReservation(r);	

//        // Show confirmation
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Reservation Confirmed");
//        alert.setHeaderText(null);
//        alert.setContentText("Your reservation is confirmed! Check your confirmation code.");
//        alert.showAndWait();
//
//        // Go back to main menu
//        SceneManager.switchTo("MainMenuUI.fxml");
    }
    
    
    // --- Helper methods ---
    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    // Show a confirmation alert
    public void showConfirmation(String msg) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Confirmed");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
        // Optionally return to main menu
        SceneManager.switchTo("MainMenuUI.fxml");
    }
}

