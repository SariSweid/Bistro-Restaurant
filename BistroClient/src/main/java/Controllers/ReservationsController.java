package Controllers;

import javafx.fxml.FXML;
import util.SceneManager;

public class ReservationsController {
	@FXML
	private void onPreviousPage() {
		SceneManager.switchTo("ManagerUI.fxml");
	}

}
