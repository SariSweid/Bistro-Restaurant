package Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneManager;

public class LoginController implements Initializable {

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
        SceneManager.switchTo("MainMenuUI.fxml");
    }
	
	//onward to the subscriber window
	@FXML
	private void onEnter() {
		/*if (userID.getText().isBlank() || membershipCode.getText().isBlank()) {
	        errorLabel.setText("Enter username and password");
	        return;*/
		SceneManager.switchTo("SubscriberUI.fxml");
	}/*should add request to server (search for the subscriber)*/
}
