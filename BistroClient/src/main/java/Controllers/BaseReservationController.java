package Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import Entities.Reservation;
import enums.ReservationStatus;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import util.SceneManager;
import handlers.ClientHandler;

public abstract class BaseReservationController {

    protected ComboBox<LocalTime> timeComboBox;
    protected DatePicker datePicker;
    protected Button confirmButton;
    protected TextField numberOfDinersField;

    // The customer ID for this reservation
    protected int customerId;

    // --- Check Availability ---
    protected void checkAvailability() {
        int diners;
        LocalDate resDate = datePicker.getValue();

        if (resDate == null) {
            showError("Please select a valid date.");
            return;
        }

        try {
            diners = Integer.parseInt(numberOfDinersField.getText());
            if (diners <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            showError("Please enter a valid number of diners");
            return;
        }

        // Send request to server
        ClientHandler.getClient().getAvailableTimes(resDate, diners);
    }

    // --- Called by server handler ---
    public void updateAvailableTimes(List<LocalTime> times) {
        Platform.runLater(() -> {
            if (times.isEmpty()) {
                // Ask server for nearest alternative dates
                ClientHandler.getClient().getNearestAvailableTimes(
                    datePicker.getValue(),
                    Integer.parseInt(numberOfDinersField.getText())
                );
                return;
            }

            timeComboBox.getItems().clear();
            timeComboBox.getItems().addAll(times);

            // Show controls
            timeComboBox.setVisible(true);
            timeComboBox.setManaged(true);
            confirmButton.setVisible(true);
            confirmButton.setManaged(true);
        });
    }

    // --- Show alternatives returned by server ---
    public void showNearestAvailableTimes(List<messages.AvailableDateTimes> alternatives) {
        if (alternatives == null || alternatives.isEmpty()) {
            showError("No available times in nearby days.");
            return;
        }

        StringBuilder msg = new StringBuilder("No availability on selected date.\n\nNearest available options:\n\n");
        for (messages.AvailableDateTimes option : alternatives) {
            msg.append(option.getDate()).append(":\n");
            for (LocalTime t : option.getTimes()) {
                msg.append("  • ").append(t).append("\n");
            }
            msg.append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alternative Dates Available");
        alert.setHeaderText("Please choose another date");
        alert.setContentText(msg.toString());
        alert.showAndWait();
    }

    // --- Submit Reservation ---
    protected void submitReservation() {
        LocalTime selectedTime = timeComboBox.getValue();
        if (selectedTime == null) {
            showError("Please select a time.");
            return;
        }

        int diners = Integer.parseInt(numberOfDinersField.getText());
        LocalDate resDate = datePicker.getValue();

        Reservation r = new Reservation(
                0,              // server assigns
                customerId,     // guest ID or subscriber ID
                diners,
                0,              // confirmation code
                resDate,
                selectedTime,
                ReservationStatus.CONFIRMED
        );

        System.out.println("Sending reservation: " + r);
        ClientHandler.getClient().addReservation(r);
    }

    // --- Helper ---
    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
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

