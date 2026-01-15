package Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import Entities.Reservation;
import Entities.User;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import util.SceneManager;

/**
 * Controller for subscriber waiting list screen.
 * Handles adding and removing a subscriber from the waiting list
 * and displaying available times received from the server.
 */
public class SubscriberWaitingListController extends BaseDisplayController implements AvailableTimesListener{

    @FXML
    private TextField numberOfDiners;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField confirmationCodeField;

    /**
     * Initializes the waiting list screen for subscribers.
     * Sets date limits and requests available times when a date is selected.
     */
    @FXML
    public void initialize() {

    	ClientHandler.getClient().setAvailableTimesListener(this);

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(
                        empty ||
                        date.isBefore(LocalDate.now()) ||
                        date.isAfter(LocalDate.now().plusMonths(1))
                );
            }
        });

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                loadTimesForDate(newDate);
            }
        });

        datePicker.setValue(LocalDate.now());
    }

    /**
     * Requests available times from the server for a given date.
     *
     * @param date selected date
     */
    private void loadTimesForDate(LocalDate date) {
        timeComboBox.getItems().clear();
        ClientHandler.getClient().getAvailableTimes(date, 1, true);
    }

    /**
     * Loads available times into the combo box.
     *
     * @param times list of available times
     */
    public void loadTimes(List<LocalTime> times) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        timeComboBox.getItems().clear();

        for (LocalTime time : times) {
            timeComboBox.getItems().add(time.format(formatter));
        }

        if (!timeComboBox.getItems().isEmpty()) {
            timeComboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Not used in this screen.
     *
     * @param reservations list of reservations
     */
    @Override
    public void showReservations(List<Reservation> reservations) {
    }

    /**
     * Sends a request to add the subscriber to the waiting list.
     */
    @FXML
    private void ontakeplace() {
        try {
            int guests = Integer.parseInt(numberOfDiners.getText());
            LocalDate date = datePicker.getValue();
            String timeStr = timeComboBox.getValue();

            if (date == null || timeStr == null) {
                showAlert("Invalid Input", "Please select date and time.");
                return;
            }

            if (guests <= 0) {
                showAlert("Invalid Input", "Number of diners must be greater than zero.");
                return;
            }

            User user = ClientHandler.getClient().getCurrentUser();
            if (user == null) {
                showAlert("Error", "No logged-in user.");
                return;
            }

            LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));

            ClientHandler.getClient().addWaitingList(
                    user.getUserId(),
                    user.getEmail(),
                    user.getPhone(),
                    guests,
                    date,
                    time
            );

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number of diners.");
        }
    }

    /**
     * Clears input fields after adding to waiting list.
     */
    public void clearAddFields() {
        numberOfDiners.clear();
    }

    /**
     * Sends a request to remove the subscriber from the waiting list.
     */
    @FXML
    private void onRemoveFromWaitingList() {
        try {
            int code = Integer.parseInt(confirmationCodeField.getText());
            ClientHandler.getClient().cancelWaiting(code);
        } catch (Exception e) {
            showAlert("Error", "Invalid confirmation code.");
        }
    }

    /**
     * Clears the confirmation code field.
     */
    public void clearConfirmationCodeField() {
        confirmationCodeField.clear();
    }

    /**
     * Returns to the previous screen.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }

    /**
     * Displays an information alert.
     *
     * @param title alert title
     * @param message alert message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Returns the confirmation code text field.
     *
     * @return confirmation code field
     */
    public TextField getConfirmationCodeField() {
        return confirmationCodeField;
    }

    
    @Override
    public void updateAvailableTimes(List<LocalTime> times) {
        loadTimes(times);
    }
}
