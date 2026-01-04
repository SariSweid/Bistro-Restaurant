package Controllers;

import javafx.fxml.FXML;
import util.SceneManager;

public class GuestReservationController {

	@FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }
	
    @FXML
    private void onCancelOrder() {
        SceneManager.switchTo("CancelReservationUI.fxml");
    }
    
    @FXML
    private void onOrderTable() {
    		SceneManager.switchTo("GuestMakeReservationUI.fxml");
    }
    
    
}
