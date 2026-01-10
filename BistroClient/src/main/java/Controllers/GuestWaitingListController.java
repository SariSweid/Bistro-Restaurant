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
import javafx.scene.control.TextField;
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
    /**
     * Initializes the controller.
     * Populates the timeComboBox with available time slots from 10:00 to 20:00 in 30-minute increments.
     */
    @FXML
    public void initialize() {
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(20, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (!start.isAfter(end)) {
            timeComboBox.getItems().add(start.format(formatter));
            start = start.plusMinutes(30);
        }

        timeComboBox.getSelectionModel().selectFirst(); 
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
            String timeStr = timeComboBox.getValue();
            LocalTime selectedTime = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate selectedDate = LocalDate.now();

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

            Integer userID = null; // guest

             ClientHandler.getClient().addWaitingList(userID, email, phone, numOfGuests, selectedDate, selectedTime);

            showAlert("Reservation Confirmed", "You have been added to the waiting list.");

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number of guests.");
        } catch (Exception e) {
            e.printStackTrace();
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
