package Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import Entities.Reservation;
import enums.ActionType;
import enums.UserRole;
import handlers.AddWaitingHandler;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import messages.RegisterRequest;
import util.SceneManager;

/**
 * Controller for managing guest waiting list reservations.
 * Allows a guest to enter either an email or a phone number, specify the number of diners, 
 * and select a time for the reservation.
 */
public class GuestWaitingListController extends  BaseDisplayController{

    @FXML
    private TextField numberOfDiners, emailOrPhone;

    @FXML
    private ComboBox<String> timeComboBox;
    
    
    @FXML
    private TextField confirmationCodeField;
    
    @FXML
    private DatePicker datePicker;
    
    
    /**
     * Initializes the controller.
     * Populates the timeComboBox with available time slots from 10:00 to 20:00 in 30-minute increments.
     */
    @FXML
    public void initialize() {

        ClientHandler.getClient().setActiveDisplayController(this);

        // Block past dates
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Block dates after 1 month
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusMonths(1)));
            }
        });

        // When user picks a date → load times
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                loadTimesForDate(newDate);
            }
        });

        // Default: today
        datePicker.setValue(LocalDate.now());
    }
    
    private void loadTimesForDate(LocalDate date) {

        timeComboBox.getItems().clear();

        // Ask server for available times
        ClientHandler.getClient().getAvailableTimes(date, 1,true); // 1 guest just to get times
    }

    public void loadTimes(List<LocalTime> times) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        timeComboBox.getItems().clear();

        for (LocalTime t : times) {
            timeComboBox.getItems().add(t.format(formatter));
        }

        if (!timeComboBox.getItems().isEmpty()) {
            timeComboBox.getSelectionModel().selectFirst();
        }
    }


    
    @Override
    public void showReservations(List<Reservation> reservations) {
      
    }

    /**
     * Handles adding the guest to the waiting list.
     * Determines if the input is an email or a phone number and sends the request to the server.
     */
    @FXML
    private void ontakeplace() {
        try {
            int numOfGuests = Integer.parseInt(numberOfDiners.getText().trim());
            String contactInfo = emailOrPhone.getText().trim();
            LocalTime selectedTime = LocalTime.parse(timeComboBox.getValue(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate selectedDate = datePicker.getValue();

            String email = null;
            String phone = null;

            if (isEmail(contactInfo)) {
                email = contactInfo;
            } else if (isPhoneNumber(contactInfo)) {
                phone = contactInfo;
            } else {
                showAlert("Invalid Input", "Please enter a valid email or phone number.");
                return;
            }

            // Generate guest ID and register guest
            int guestId = generateUniqueGuestId();
            RegisterRequest register = new RegisterRequest(
                    guestId, null, email, phone, null, 0, UserRole.GUEST
            );
            ClientHandler.getClient().register(register);

            // Use the real userID for waiting list
            ClientHandler.getClient().addWaitingList(
                    guestId, email, phone, numOfGuests, selectedDate, selectedTime
            );

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number of guests.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int generateUniqueGuestId() {
        return 10000 + (int)(Math.random() * 10000);
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

          
            ClientHandler.getClient().cancelWaiting(code);

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid confirmation code.");
        }
    }
    
    public void clearAddFields() {
        numberOfDiners.clear();
        emailOrPhone.clear();
    }

    /**
     * Returns to the main menu.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    /**
     * Determines if the input string is a valid email.
     * @param input the string to check
     * @return true if input contains "@", false otherwise
     */
    private boolean isEmail(String input) {
        return input.contains("@");
    }
    
    
    public void clearConfirmationCodeField() {
        confirmationCodeField.clear();
    }

    public TextField getConfirmationCodeField() {
		return confirmationCodeField;
	}

	public void setConfirmationCodeField(TextField confirmationCodeField) {
		this.confirmationCodeField = confirmationCodeField;
	}
	
    /**
     * Determines if the input string is a valid phone number.
     * @param input the string to check
     * @return true if input is digits optionally starting with "+", false otherwise
     */
    private boolean isPhoneNumber(String input) {
        return input.matches("\\+?\\d+");
    }

    /**
     * Shows an alert dialog with a given title and message.
     * @param title the title of the alert
     * @param message the message to display
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
