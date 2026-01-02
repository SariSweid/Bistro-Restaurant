package Controllers;

import java.awt.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.SceneManager;

public class GuestMakeReservationController {
	@FXML
	private void onPreviousPage() {
	    SceneManager.switchTo("GuestReservationUI.fxml");
	}
	
	@FXML

	private void onSubmitOrder() {
	    
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("Order Confirmed");
	    alert.setHeaderText(null); 
	    alert.setContentText("Thank you for choosing us!");

	 
	    alert.showAndWait();
		/*if (userID.getText().isBlank() || membershipCode.getText().isBlank() || 
		 * name.getText().isBlank() || phone.getText().isBlank() || email.getText().isBlank()
		 * || userName.getText().isBlank()) {
	        errorLabel.setText("Fill all Fields");
	        return;*/
		System.out.println("Thanks For Choosing Us!");
		SceneManager.switchTo("MainMenuUI.fxml");
	}/*we should make sure user's info is updated in the DB  */
}
