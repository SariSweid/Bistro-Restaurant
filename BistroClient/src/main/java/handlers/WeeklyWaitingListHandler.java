package handlers;

import Entities.WaitingListEntry;
import Controllers.WaitingListController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.util.List;

/**
 * Handles server responses containing the weekly waiting list entries.
 * Updates the WaitingListController's table with the received list,
 * or shows an error alert if the data is invalid.
 */
public class WeeklyWaitingListHandler implements ResponseHandler {

    /**
     * Reference to the WaitingListController that manages the UI table.
     */
    private WaitingListController controller;

    /**
     * Constructs a new WeeklyWaitingListHandler with the specified controller.
     *
     * @param controller the WaitingListController to update
     */
    public WeeklyWaitingListHandler(WaitingListController controller) {
        this.controller = controller;
    }

    /**
     * Handles the server response for the weekly waiting list.
     * If the data is a valid list of WaitingListEntry objects, updates
     * the table in the controller. Otherwise, displays an error alert.
     *
     * @param data the server response object, expected to be a List of WaitingListEntry
     */
    @Override
    public void handle(Object data) {
        if (data instanceof List<?>) {
            @SuppressWarnings("unchecked")
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
