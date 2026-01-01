package Controllers;

import javafx.fxml.FXML;

import util.SceneManager;

public class TablesController {

	@FXML
	private void onLoadTable() { }
    @FXML
    private void onEdit() {}

    @FXML
    private void onSave() {}

    @FXML
    private void onDelete() {}

    @FXML
    private void onInsert() {}

	@FXML
	private void onPreviousPage() {
		SceneManager.switchTo("SupervisorUI.fxml");
	}
}
