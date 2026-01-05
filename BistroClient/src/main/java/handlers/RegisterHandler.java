package handlers;

import common.ServerResponse;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import util.SceneManager;

public class RegisterHandler implements ResponseHandler {

	@Override
	public void handle(Object data) {
	    if (!(data instanceof ServerResponse response)) {
	        System.out.println("RegisterHandler: Invalid data received");
	        return;
	    }

	    Object obj = response.getData();
	    if (obj instanceof Entities.User user) {
	        if (user.getRole() == enums.UserRole.GUEST) {
	       
	            return;
	        }
	    }
	    Platform.runLater(() -> {
	        Alert alert = new Alert(response.isSuccess() ?
	                Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);

	        alert.setTitle("Registration");
	        alert.setHeaderText(null);
	        alert.setContentText(response.getMessage());
	        alert.showAndWait();

	        if (response.isSuccess()) {
	            SceneManager.switchTo("SupervisorUI.fxml");
	        }
	    });
	}


}