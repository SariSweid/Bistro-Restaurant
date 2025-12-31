package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class RestaurantSettingsController {

    @FXML
    private TextField openingTimeField;

    @FXML
    private TextField closingTimeField;

    @FXML
    private TextField specialDateField;

    @FXML
    private TextField specialNoteField;

    @FXML
    private TableView<?> specialDatesTable;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> noteColumn;

    @FXML
    public void updateOpeningHours(ActionEvent event) {
    }

    @FXML
    public void updateClosingHours(ActionEvent event) {
    }

    @FXML
    public void addSpecialDates(ActionEvent event) {
    }

    @FXML
    public void updateSpecialDates(ActionEvent event) {
    }

    @FXML
    public void previousPage(ActionEvent event) {
        
    }
}
