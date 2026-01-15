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

public class SubscriberWaitingListController extends BaseDisplayController implements AvailableTimesListener {

    @FXML
    private TextField numberOfDiners;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField confirmationCodeField;

    /**
     * Initializes the subscriber waiting list controller.
     * Sets up date picker limits, available times listener,
     * and saves this controller as active in the ClientHandler.
     */
    @FXML
    public void initialize() {
        ClientHandler.getClient().setAvailableTimesListener(this);
        ClientHandler.getClient().setActiveDisplayController(this);

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

        datePicker.setValue(LocalDate.now());
    }

    /**
     * Requests available times from the server for the given date.
     *
     * @param date The date for which to retrieve available times.
     */
    private void loadTimesForDate(LocalDate date) {
        timeComboBox.getItems().clear();
        ClientHandler.getClient().getAvailableTimes(date, 1, true);
    }

    /**
     * Loads available times into the combo box.
     *
     * @param times List of LocalTime objects representing available slots.
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
     * @param reservations List of reservations.
     */
    @Override
    public void showReservations(List<Reservation> reservations) {}

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
     * Sends a request to remove the subscriber from the waiting list by confirmation code.
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
     * Clears the confirmation code input field.
     */
    public void clearConfirmationCodeField() {
        confirmationCodeField.clear();
    }

    /**
     * Returns to the previous subscriber UI screen.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }

    /**
     * Shows an information alert dialog.
     *
     * @param title   Title of the alert.
     * @param message Message content of the alert.
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
     * @return TextField used for entering confirmation code.
     */
    public TextField getConfirmationCodeField() {
        return confirmationCodeField;
    }

    /**
     * Receives updated available times from the server.
     *
     * @param times List of available LocalTime slots.
     */
    @Override
    public void updateAvailableTimes(List<LocalTime> times) {
        loadTimes(times);
    }
}
