package handlers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import util.SceneManager;

public class OrderController {

    
    @FXML private TableView<?> reservationsTable;
    @FXML private TableColumn<?, ?> idColumn;
    @FXML private TableColumn<?, ?> dateColumn;
    @FXML private TableColumn<?, ?> guestsColumn;
    @FXML private TableColumn<?, ?> confirmationCodeColumn;

    
    @FXML private CheckBox updateCheckBox;
    @FXML private TextField dateField;
    @FXML private TextField guestsField;
    @FXML private TextField timeField;

    
    @FXML private Button updateButton;

    @FXML
    private void initialize() {
        // Make sure everything starts disabled
        setUpdateControlsEnabled(false);

        // dependant on checkbox
        updateCheckBox.selectedProperty().addListener((obs, oldVal, isChecked) -> {
            setUpdateControlsEnabled(isChecked);
        });
    }

    private void setUpdateControlsEnabled(boolean enabled) {
        dateField.setDisable(!enabled);
        guestsField.setDisable(!enabled);
        timeField.setDisable(!enabled);
        updateButton.setDisable(!enabled);
    }

    
    @FXML
    private void onUpdateReservation() {
        if (!updateCheckBox.isSelected()) {
            return;
        } 

        String date = dateField.getText();
        String guests = guestsField.getText();
        String time = timeField.getText();
        /*yet to connect to datbase and update there*/
        
        
    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberMenuUI.fxml");
    }
}

