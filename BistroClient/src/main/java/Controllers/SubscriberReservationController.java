package Controllers;

import javafx.fxml.FXML;
import util.SceneManager;

public class SubscriberReservationController {
	@FXML
	private void onPreviousPage() {
	    SceneManager.switchTo("SubscriberUI.fxml");
	}
	
	@FXML
	private void onSubmitOrder() {
		/*if (userID.getText().isBlank() || membershipCode.getText().isBlank() || 
		 * name.getText().isBlank() || phone.getText().isBlank() || email.getText().isBlank()
		 * || userName.getText().isBlank()) {
	        errorLabel.setText("Fill all Fields");
	        return;*/
		System.out.println("Thanks For Choosing Us!");
	}/*we should make sure user's info is updated in the DB  */
}
