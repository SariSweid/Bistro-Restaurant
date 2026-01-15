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

    @FXML
    public void initialize() {
        client = ClientHandler.getClient();
        client.setAvailableTimesListener(this);
        client.setActiveDisplayController(this);

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusMonths(1)));
            }
        });

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) loadTimesForDate(newDate);
        });

        numberOfDiners.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) {
                LocalDate date = datePicker.getValue();
                if (date != null) loadTimesForDate(date);
            }
        });

        datePicker.setValue(LocalDate.now());
    }

    private void loadTimesForDate(LocalDate date) {
        timeComboBox.getItems().clear();
        int guests = 1;
        try {
            guests = Integer.parseInt(numberOfDiners.getText().trim());
        } catch (NumberFormatException ignored) {}
        client.getAvailableTimes(date, guests, true);
    }

    public void loadTimes(List<LocalTime> times) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        timeComboBox.getItems().clear();
        for (LocalTime t : times) timeComboBox.getItems().add(t.format(formatter));
        if (!timeComboBox.getItems().isEmpty()) timeComboBox.getSelectionModel().selectFirst();
    }

    @Override
    public void showReservations(List<Reservation> reservations) {}

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

    public void clearAddFields() {
        numberOfDiners.clear();
        emailOrPhone.clear();
    }

    public void clearConfirmationCodeField() {
        confirmationCodeField.clear();
    }

    public TextField getConfirmationCodeField() {
        return confirmationCodeField;
    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    private boolean isEmail(String input) {
        return input.contains("@");
    }

    private boolean isPhoneNumber(String input) {
        return input.matches("\\+?\\d+");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void updateAvailableTimes(List<LocalTime> times) {
        loadTimes(times);
    }
}
