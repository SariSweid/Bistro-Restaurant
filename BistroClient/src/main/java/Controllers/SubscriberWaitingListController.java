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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import util.SceneManager;

public class SubscriberWaitingListController extends  BaseDisplayController {

    @FXML
    private TextField numberOfDiners;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField confirmationCodeField;

    @FXML
    public void initialize() {

        // Set this controller as the active display controller
        ClientHandler.getClient().setActiveDisplayController(this);

        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(20, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (!start.isAfter(end)) {
            timeComboBox.getItems().add(start.format(formatter));
            start = start.plusMinutes(30);
        }

        timeComboBox.getSelectionModel().selectFirst();
        datePicker.setValue(LocalDate.now());
    }
    
    
    @Override
    public void showReservations(List<Reservation> reservations) {
      
    }

    @FXML
    private void ontakeplace() {
        try {
            int numOfGuests = Integer.parseInt(numberOfDiners.getText());
            LocalDate selectedDate = datePicker.getValue();
            String timeStr = timeComboBox.getValue();
            LocalTime selectedTime = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));

            Integer userID = ClientHandler.getClient().getCurrentUserId();

            ClientHandler.getClient().addWaitingList(userID, null,null, numOfGuests, selectedDate, selectedTime);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Reservation Confirmed");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number of diners.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void clearAddFields() {
        numberOfDiners.clear();

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

    
    public void clearConfirmationCodeField() {
        confirmationCodeField.clear();
    }





    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

	public TextField getConfirmationCodeField() {
		return confirmationCodeField;
	}

	public void setConfirmationCodeField(TextField confirmationCodeField) {
		this.confirmationCodeField = confirmationCodeField;
	}
}
