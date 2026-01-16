package Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import Entities.Reservation;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import util.SceneManager;

/**
 * Controller for the guest waiting list UI.
 * Handles adding guests to the waiting list, removing them, and displaying available times.
 */
public class GuestWaitingListController extends BaseDisplayController implements AvailableTimesListener {

    @FXML
    private TextField numberOfDiners, emailOrPhone;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private TextField confirmationCodeField;

    @FXML
    private DatePicker datePicker;

    private ClientHandler client;

    /**
     * Initializes the controller.
     * Sets up the date picker, listeners for number of diners, and registers this controller as the active display controller.
     */
    @FXML
    public void initialize() {
        client = ClientHandler.getClient();
        client.setAvailableTimesListener(this);
        client.setActiveDisplayController(this);

        datePicker.setDayCellFactory(_ -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusMonths(1)));
            }
        });

        datePicker.valueProperty().addListener((_, _, newDate) -> {
            if (newDate != null) loadTimesForDate(newDate);
        });

        numberOfDiners.textProperty().addListener((_, _, newVal) -> {
            if (!newVal.isBlank()) {
                LocalDate date = datePicker.getValue();
                if (date != null) loadTimesForDate(date);
            }
        });

        datePicker.setValue(LocalDate.now());
    }

    /**
     * Loads available times for a specific date and number of guests.
     * @param date the selected date
     */
    private void loadTimesForDate(LocalDate date) {
        timeComboBox.getItems().clear();
        int guests = 1;
        try {
            guests = Integer.parseInt(numberOfDiners.getText().trim());
        } catch (NumberFormatException ignored) {}
        client.getAvailableTimes(date, guests, true);
    }

    /**
     * Loads a list of available times into the timeComboBox.
     * @param times a list of available LocalTime objects
     */
    public void loadTimes(List<LocalTime> times) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        timeComboBox.getItems().clear();
        for (LocalTime t : times) timeComboBox.getItems().add(t.format(formatter));
        if (!timeComboBox.getItems().isEmpty()) timeComboBox.getSelectionModel().selectFirst();
    }

    @Override
    public void showReservations(List<Reservation> reservations) {}

    /**
     * Handles adding a guest to the waiting list.
     * Validates input fields and sends the request to the client handler.
     */
    @FXML
    private void ontakeplace() {
        try {
            String dinersText = numberOfDiners.getText();
            String contactInfo = emailOrPhone.getText();
            String timeText = timeComboBox.getValue();
            LocalDate selectedDate = datePicker.getValue();

            if (dinersText == null || dinersText.isBlank()) {
                showAlert("Invalid Input", "Please enter number of guests.");
                return;
            }
            if (contactInfo == null || contactInfo.isBlank()) {
                showAlert("Invalid Input", "Please enter an email or phone number.");
                return;
            }
            if (selectedDate == null) {
                showAlert("Invalid Input", "Please select a date.");
                return;
            }
            if (timeText == null || timeText.isBlank()) {
                showAlert("Invalid Input", "Please select a time.");
                return;
            }

            int numOfGuests = Integer.parseInt(dinersText.trim());
            LocalTime selectedTime = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));

            String email = null;
            String phone = null;
            if (isEmail(contactInfo)) email = contactInfo;
            else if (isPhoneNumber(contactInfo)) phone = contactInfo;
            else {
                showAlert("Invalid Input", "Please enter a valid email or phone number.");
                return;
            }

            client.addWaitingList(null, email, phone, numOfGuests, selectedDate, selectedTime);

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number of guests.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles removing a guest from the waiting list using a confirmation code.
     */
    @FXML
    private void onRemoveFromWaitingList() {
        String codeText = confirmationCodeField.getText();
        if (codeText == null || codeText.isEmpty()) {
            showAlert("Error", "Please enter your confirmation code.");
            return;
        }
        try {
            int code = Integer.parseInt(codeText);
            client.cancelWaiting(code);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid confirmation code.");
        }
    }

    /**
     * Clears the input fields for adding a guest.
     */
    public void clearAddFields() {
        numberOfDiners.clear();
        emailOrPhone.clear();
    }

    /**
     * Clears the confirmation code field.
     */
    public void clearConfirmationCodeField() {
        confirmationCodeField.clear();
    }

    /**
     * Returns the confirmation code TextField.
     * @return the TextField for confirmation code
     */
    public TextField getConfirmationCodeField() {
        return confirmationCodeField;
    }

    /**
     * Navigates back to the main menu UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    /**
     * Simple check to see if input is an email.
     * @param input the input string
     * @return true if it contains "@", false otherwise
     */
    private boolean isEmail(String input) {
        return input.contains("@");
    }

    /**
     * Simple check to see if input is a phone number.
     * @param input the input string
     * @return true if it contains only digits (and optional +), false otherwise
     */
    private boolean isPhoneNumber(String input) {
        return input.matches("\\+?\\d+");
    }

    /**
     * Shows an alert dialog with the given title and message.
     * @param title the title of the alert
     * @param message the message content
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Called when available times are updated by the client.
     * @param times list of available LocalTime objects
     */
    @Override
    public void updateAvailableTimes(List<LocalTime> times) {
        loadTimes(times);
    }
}
