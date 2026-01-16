package handlers;

import java.util.List;

import Controllers.TablesController;
import Entities.Table;
import common.ServerResponse;

/**
 * Handler for processing the server response when retrieving all tables.
 * Updates the TablesController with the list of tables if the request is successful,
 * or displays an error message if it fails.
 */
public class GetAllTablesHandler implements ResponseHandler {

    /**
     * Handles the server response for fetching all tables.
     * If successful, updates the table list in the TablesController.
     * If failed, shows an error message from the server.
     *
     * @param data the response data from the server, expected to be a ServerResponse containing a List<Table>
     */
    @Override
    public void handle(Object data) {
        ServerResponse response = (ServerResponse) data;

        TablesController controller = ClientHandler.getClient().getTablesController();

        if (response.isSuccess()) {
            @SuppressWarnings("unchecked")
            List<Table> tables = (List<Table>) response.getData();
            controller.updateTableList(tables);
        } else {
            controller.showError(response.getMessage());
        }
    }
}
