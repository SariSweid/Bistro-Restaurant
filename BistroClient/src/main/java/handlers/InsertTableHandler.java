package handlers;

import Controllers.TablesController;
import common.ServerResponse;

/**
 * Handles server responses related to inserting a new table into the system.
 * This handler updates the TablesController UI based on the success or failure
 * of the insert table operation.
 */
public class InsertTableHandler implements ResponseHandler {

    /**
     * Processes the server response for a table insertion request.
     * If the operation was successful, an informational message is shown
     * and the tables list is reloaded.
     * If the operation failed, an error message is displayed.
     *
     * @param data the server response object containing the result of the insert operation
     */
    @Override
    public void handle(Object data) {
        ServerResponse response = (ServerResponse) data;

        TablesController controller = ClientHandler.getClient().getTablesController();

        if (response.isSuccess()) {
            controller.showInfo(response.getMessage());
            controller.reloadTables();
        } else {
            controller.showError(response.getMessage());
        }
    }
}

