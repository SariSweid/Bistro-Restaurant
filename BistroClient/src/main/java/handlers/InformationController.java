package handlers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import util.SceneManager;

public class InformationController {
	//return back to the main page
		@FXML
		private void onPreviousPage() {
		    SceneManager.switchTo("MainMenuUI.fxml");
		}
		
		@FXML
		private void onSubmitChanges() {
			/*if (userID.getText().isBlank() || membershipCode.getText().isBlank() || 
			 * name.getText().isBlank() || phone.getText().isBlank() || email.getText().isBlank()
			 * || userName.getText().isBlank()) {
		        errorLabel.setText("Fill all Fields");
		        return;*/
			SceneManager.switchTo("RestaurantSettingsUI.fxml");
		}/*we should make sure user's info is updated in the DB  */
}

	
	

