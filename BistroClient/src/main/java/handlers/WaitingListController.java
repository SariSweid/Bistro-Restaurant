package handlers;

import javafx.fxml.FXML;
import util.SceneManager;

public class WaitingListController {
	@FXML
	private void onPreviousPage() {
		SceneManager.switchTo("ManagerUI.fxml");
	}
}
