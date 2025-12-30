package Controllers;

import javafx.fxml.FXML;
import util.SceneManager;

public class RegisterationController {
	
	//return back to the main page
	@FXML
	private void onPreviousPage() {
	    SceneManager.switchTo("MainMenuUI.fxml");
	}
	
	@FXML
	private void onEnter() {
		/*if (userID.getText().isBlank() || membershipCode.getText().isBlank() || 
		 * name.getText().isBlank() || phone.getText().isBlank() || email.getText().isBlank()
		 * || userName.getText().isBlank()) {
	        errorLabel.setText("Fill all Fields");
	        return;*/
		SceneManager.switchTo("RestaurantSettingsUI.fxml");
	}/*we should make sure new user is added to the DB  */
}
