package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import util.SceneManager;

public class OrderController {

    
    @FXML private TableView<?> reservationsTable;
    @FXML private TableColumn<?, ?> idColumn;
    @FXML private TableColumn<?, ?> dateColumn;
    @FXML private TableColumn<?, ?> guestsColumn;
    @FXML private TableColumn<?, ?> confirmationCodeColumn;

    
    
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}

