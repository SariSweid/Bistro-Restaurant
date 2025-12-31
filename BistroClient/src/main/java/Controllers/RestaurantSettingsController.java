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
        System.out.println("Updating opening hours...");
    }

    @FXML
    public void updateClosingHours(ActionEvent event) {
        System.out.println("Updating closing hours...");
    }

    @FXML
    public void addSpecialDates(ActionEvent event) {
        System.out.println("Adding special date...");
    }

    @FXML
    public void updateSpecialDates(ActionEvent event) {
        System.out.println("Updating special date...");
    }

    @FXML
    public void previousPage(ActionEvent event) {
        System.out.println("Going to previous page...");
    }
}
