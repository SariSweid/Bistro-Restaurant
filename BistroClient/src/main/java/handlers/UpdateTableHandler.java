package handlers;

import Controllers.TablesController;
import common.ServerResponse;

/**
 * Handles server responses related to updating table information.
 * Based on the response, it updates the TablesController by reloading
 * the table data or showing an error message.
 */
public class UpdateTableHandler implements ResponseHandler {

    /**
     * Processes the server response for a table update.
     * If the update was successful, it shows an informational message
     * and reloads the tables in the controller.
     * If the update failed, it shows an error message instead.
     *
     * @param data the server response object containing success status and message
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
