package handlers;

import Controllers.TablesController;
import common.ServerResponse;

/**
 * Handles the server response for deleting a table.
 * 
 * Processes the response from the server after a delete table request.
 * If the deletion is successful, it shows an informational message and reloads the tables.
 * If the deletion fails, it shows an error message.
 */
public class DeleteTableHandler implements ResponseHandler {

    /**
     * Processes the server response for deleting a table.
     *
     * @param data the response object received from the server; expected to be a ServerResponse
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
