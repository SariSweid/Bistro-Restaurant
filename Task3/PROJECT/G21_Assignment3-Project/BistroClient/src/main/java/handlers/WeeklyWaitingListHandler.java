package handlers;

import Entities.WaitingListEntry;
import Controllers.WaitingListController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles server responses containing weekly waiting list entries.
 * Updates the WaitingListController's table with entries whose status is null.
 */
public class WeeklyWaitingListHandler implements ResponseHandler {

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
     * Filters the entries to only include those with a null status before updating the table.
     * Displays an error alert if the received data is invalid.
     *
     * @param data the server response object, expected to be a List of WaitingListEntry
     */
    @Override
    public void handle(Object data) {
        if (data instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<WaitingListEntry> list = (List<WaitingListEntry>) data;

            List<WaitingListEntry> filteredList = list.stream()
                    .filter(entry -> entry.getExitReason() == null)
                    .collect(Collectors.toList());

            Platform.runLater(() -> {
                controller.getWaitingListTable().getItems().clear();
                controller.getWaitingListTable().getItems().addAll(filteredList);
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
