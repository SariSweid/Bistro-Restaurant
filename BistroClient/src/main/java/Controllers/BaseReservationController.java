package Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Entities.Reservation;
import enums.ReservationStatus;
import handlers.ClientHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import util.SceneManager;

public abstract class BaseReservationController implements AvailableTimesListener {

    @FXML
    protected ComboBox<LocalTime> timeComboBox;

    @FXML
    protected DatePicker datePicker;

    @FXML
    protected Button confirmButton;

    @FXML
    protected TextField numberOfDinersField;
    
    protected int diners = 0;

    @SuppressWarnings("unused")
	@FXML
    public void initialize() {
	    	ClientHandler.getClient().setActiveReservationController(this);
	    	ClientHandler.getClient().setAvailableTimesListener(this);

        if (datePicker != null) {
            datePicker.valueProperty().addListener((obs, oldDate, newDate) -> hideTimeSelection());
        }
    }

    protected int getNumberOfDiners() throws NumberFormatException {
        String text = numberOfDinersField.getText();
        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Number of diners field is empty");
        }
        int diners = Integer.parseInt(text.trim());
        if (diners <= 0) throw new NumberFormatException("Number of diners must be > 0");
        return diners;
    }

    protected void checkAvailability() {
        LocalDate resDate = datePicker.getValue();

        if (resDate == null) {
            showError("Please select a valid date.");
            return;
        }

        try {
        		this.diners = getNumberOfDiners();
        } catch (NumberFormatException e) {
            showError("Please enter a valid number of diners");
            return;
        }

        ClientHandler.getClient().getAvailableTimes(resDate, diners, false);
    }

    public void updateAvailableTimes(List<LocalTime> times) {
        Platform.runLater(() -> {
            int diners;
            try {
                diners = getNumberOfDiners();
            } catch (NumberFormatException e) {
                showError("Please enter a valid number of diners");
                return;
            }

            LocalDate date = datePicker.getValue();
            System.out.println("UpdateAvailableTimes called. Date: " + date + ", diners: " + diners + ", times received: " + times);

            if (times.isEmpty()) {
                if (date != null) {
                    ClientHandler.getClient().getNearestAvailableTimes(date, diners);
                } else {
                    showError("Please select a date before checking availability");
                }
                return;
            }

            timeComboBox.getItems().clear();
            timeComboBox.getItems().addAll(times);
            timeComboBox.setVisible(true);
            timeComboBox.setManaged(true);
            confirmButton.setVisible(true);
            confirmButton.setManaged(true);
        });
    }


    public void showNearestAvailableTimes(List<messages.AvailableDateTimes> alternatives) {
        if (alternatives == null || alternatives.isEmpty()) {
            showError("No available reservations on the selected date or nearby dates.");
            return;
        }

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

        int index = choices.indexOf(result.get());
        LocalDate chosenDate = dates.get(index);
        LocalTime chosenTime = times.get(index);

        submitAlternativeReservation(chosenDate, chosenTime, diners);
    }

    protected void submitReservation() {
        LocalTime selectedTime = timeComboBox.getValue();
        if (selectedTime == null) {
            showError("Please select a time.");
            return;
        }

        int diners;
        try {
            diners = getNumberOfDiners();
        } catch (NumberFormatException e) {
            showError("Please enter a valid number of diners");
            return;
        }

        LocalDate resDate = datePicker.getValue();
        int customerId = ClientHandler.getClient().getCurrentUserId();
        Reservation r = new Reservation(0, customerId, diners, 0, resDate, selectedTime, ReservationStatus.CONFIRMED, true);
        ClientHandler.getClient().addReservation(r);

        resetForm();
    }

    protected void submitAlternativeReservation(LocalDate date, LocalTime time, int diners) {
        int customerId = ClientHandler.getClient().getCurrentUserId();
        Reservation r = new Reservation(0, customerId, diners, 0, date, time, ReservationStatus.CONFIRMED, true);
        ClientHandler.getClient().addReservation(r);

        resetForm();
    }

    public void showError(String msg) {
        SceneManager.showError(msg);
    }

    public void showConfirmation(String msg) {
        SceneManager.showInfo(msg);
    }

    protected void setupDatePickerLimits() {
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusMonths(1);

        datePicker.setDayCellFactory(_ -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty || date == null) {
                    setDisable(true);
                    return;
                }

                if (date.isBefore(today) || date.isAfter(maxDate)) {
                    setDisable(true);
                }
            }
        });
    }

    protected void resetForm() {
        datePicker.setValue(null);
        numberOfDinersField.clear();
        timeComboBox.getItems().clear();
        timeComboBox.setVisible(false);
        timeComboBox.setManaged(false);
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