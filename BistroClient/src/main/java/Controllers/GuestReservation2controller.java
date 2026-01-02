package Controllers;

import javafx.fxml.FXML;
import util.SceneManager;

public class GuestReservation2controller {

	
    @FXML
    private void onupdateorder() {
        SceneManager.switchTo("GuestUpdateReservationUI.fxml");
    }
    
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }
    
    @FXML
    private void onordertable() {
    	SceneManager.switchTo("GuestMakeReservationUI.fxml");
    }
    
    
}
