package Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneManager;

public class RegisterationController implements Initializable {

    @FXML
    private ImageView logoImageView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/BistroLogo.png"));
            if (image != null) {
                logoImageView.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//return back to the main page
	@FXML
	private void onPreviousPage() {
	    SceneManager.switchTo("SupervisorUI.fxml");
	}
	
	@FXML
	private void onEnter() {
		/*if (userID.getText().isBlank() || membershipCode.getText().isBlank() || 
		 * name.getText().isBlank() || phone.getText().isBlank() || email.getText().isBlank()
		 * || userName.getText().isBlank()) {
	        errorLabel.setText("Fill all Fields");
	        return;*/

		    
		    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
		    alert.setTitle("Registration");
		    alert.setHeaderText(null);
		    alert.setContentText("Registration was successful!");
		    alert.showAndWait();

		    // לאחר ההודעה ניתן לעבור למסך הבא
		    SceneManager.switchTo("SupervisorUI.fxml");
		}
 // not sure if he need to switch to this scene after register
	}/*we should make sure new user is added to the DB  */

