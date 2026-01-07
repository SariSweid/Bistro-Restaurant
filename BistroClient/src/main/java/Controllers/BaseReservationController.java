package Controllers;

import java.time.LocalDate;
import javafx.scene.control.DateCell;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import Entities.Reservation;
import enums.ReservationStatus;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import util.SceneManager;
import handlers.ClientHandler;

public abstract class BaseReservationController {
	@FXML
    protected ComboBox<LocalTime> timeComboBox;
	
	@FXML
    protected DatePicker datePicker;
	
	@FXML
    protected Button confirmButton;
	
	@FXML
    protected TextField numberOfDinersField;

	@FXML
	public void initialize() {
	    ClientHandler.getClient().setActiveReservationController(this);

	    // Hide times when date changes
	    if (datePicker != null) {
	        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
	            hideTimeSelection();
	        });
	    }
	}

 
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
        System.out.println("updateAvailableTimes called with: " + times);

        Platform.runLater(() -> {
            if (times.isEmpty()) {
                // No times on selected date → ask for nearest available times
                ClientHandler.getClient().getNearestAvailableTimes(
                    datePicker.getValue(),
                    Integer.parseInt(numberOfDinersField.getText())
                );
                return;
            }

            // We have available times → show them
            timeComboBox.getItems().clear();
            timeComboBox.getItems().addAll(times);

            timeComboBox.setVisible(true);
            timeComboBox.setManaged(true);
            confirmButton.setVisible(true);
            confirmButton.setManaged(true);
        });
    }


    // --- Show alternatives returned by server ---
    public void showNearestAvailableTimes(List<messages.AvailableDateTimes> alternatives) {
        if (alternatives == null || alternatives.isEmpty()) {
        		showError("No available reservations on the selected date or nearby dates.");
        		return;
        }

        // Build a flat list: each entry is ONE date+time
        List<String> choices = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();
        List<LocalTime> times = new ArrayList<>();

        for (messages.AvailableDateTimes option : alternatives) {
            for (LocalTime t : option.getTimes()) {
                choices.add(option.getDate() + " → " + t);
                dates.add(option.getDate());
                times.add(t);
            }
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Nearest Available Times");
        dialog.setHeaderText("No availability on selected date.");
        dialog.setContentText("Choose an alternative date & time:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        // Find index of selected item
        int index = choices.indexOf(result.get());
        LocalDate chosenDate = dates.get(index);
        LocalTime chosenTime = times.get(index);

        // Submit immediately
        submitAlternativeReservation(chosenDate, chosenTime);
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
        
        
        int customerId = ClientHandler.getClient().getCurrentUserId();

        Reservation r = new Reservation(
                0,             // server assigns
                customerId,     // guest ID or subscriber ID
                diners,
                0,              // confirmation code
                resDate,
                selectedTime,
                ReservationStatus.CONFIRMED
        );

        System.out.println("Sending reservation: " + r);
        ClientHandler.getClient().addReservation(r);

	     // Reset UI after sending reservation
	     resetForm();
    }
    
    // -- Submit alternative reservation --
    protected void submitAlternativeReservation(LocalDate date, LocalTime time) {
        int diners = Integer.parseInt(numberOfDinersField.getText());
        int customerId = ClientHandler.getClient().getCurrentUserId();

        Reservation r = new Reservation(
                0,
                customerId,
                diners,
                0,
                date,
                time,
                ReservationStatus.CONFIRMED
        );

        ClientHandler.getClient().addReservation(r);
        resetForm();
    }


    // --- Helper ---
    public void showError(String msg) {
        SceneManager.showError(msg);
    }
    
    public void showConfirmation(String msg) {
        SceneManager.showInfo(msg);
    }
    
 // Limit DatePicker one hour from now TO one month
    protected void setupDatePickerLimits() {

        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusMonths(1);

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty || date == null) {
                    setDisable(true);
                    return;
                }
                
                LocalDate today = LocalDate.now();
                LocalDate maxDate = today.plusMonths(1);

                // Disable past dates and dates beyond 1 month
                if (date.isBefore(today) || date.isAfter(maxDate)) {
                    setDisable(true);
                }
            }
        });
    }
    
    protected void resetForm() {
        // Clear date
        datePicker.setValue(null);

        // Clear diners
        numberOfDinersField.clear();

        // Hide time selection
        timeComboBox.getItems().clear();
        timeComboBox.setVisible(false);
        timeComboBox.setManaged(false);

        // Hide confirm button
        confirmButton.setVisible(false);
        confirmButton.setManaged(false);
    }

    protected void hideTimeSelection() {
        timeComboBox.getItems().clear();
        timeComboBox.setVisible(false);
        timeComboBox.setManaged(false);

        confirmButton.setVisible(false);
        confirmButton.setManaged(false);
    }

}

