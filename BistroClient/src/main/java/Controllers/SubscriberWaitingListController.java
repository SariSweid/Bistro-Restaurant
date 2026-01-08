package Controllers;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import util.SceneManager;

public class SubscriberWaitingListController {

    @FXML
    private TextField numberOfDiners, emailOrPhone;

    @FXML
    private ComboBox<String> timeComboBox;

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

    @FXML
    private void ontakeplace() {

    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}
