package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.SceneManager;

public class GuestReservationController {
	
	// This should be set when guest session starts
    private int currentGuestId;

    public void setCurrentGuestId(int guestId) {
        this.currentGuestId = guestId;
    }

	@FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }
	
    @FXML
    private void onCancelOrder() {
    	    try {
    	        // Load FXML
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui//GuestCancelReservationUI.fxml"));
    	        Parent root = loader.load();

    	        // Pass guestId to the popup controller
            GuestCancelReservationController controller = loader.getController();
            controller.setGuestId(currentGuestId);

    	        Stage stage = new Stage();
    	        stage.setScene(new Scene(root));
    	        stage.setTitle("Cancel Reservation");
    	        stage.show();

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}

    
    @FXML
    private void onOrderTable() {
    		SceneManager.switchTo("GuestMakeReservationUI.fxml");
    }
    
    
}
