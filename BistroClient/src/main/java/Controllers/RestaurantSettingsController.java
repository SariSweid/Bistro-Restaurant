package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.SceneManager;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class RestaurantSettingsController {


    @FXML
    public void updateOpeningHours() {
    }

    @FXML
    public void updateClosingHours() {
    }

    @FXML
    public void addSpecialDates() {
    }

    @FXML
    public void updateSpecialDates() {
    }

    @FXML
    public void previousPage() {
    	SceneManager.switchTo("SupervisorUI.fxml");
    }
}
