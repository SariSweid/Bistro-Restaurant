package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import util.SceneManager;

public class ReportController {
	
	@FXML
	private void onPreviousPage() {
		SceneManager.switchTo("ManagerUI.fxml");
	}
	
	
}
