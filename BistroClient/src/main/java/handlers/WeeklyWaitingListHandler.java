package handlers;

import Entities.WaitingListEntry;
import Controllers.WaitingListController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.util.List;

public class WeeklyWaitingListHandler implements ResponseHandler {

    private WaitingListController controller;

    public WeeklyWaitingListHandler(WaitingListController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(Object data) {
    	
    	
    	
        if (data instanceof List<?>) {
        	
        	
            List<WaitingListEntry> list = (List<WaitingListEntry>) data;

            Platform.runLater(() -> {
                controller.getWaitingListTable().getItems().clear();
                controller.getWaitingListTable().getItems().addAll(list);
            });

        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load weekly waiting list");
                alert.showAndWait();
            });
        }
    }
}
