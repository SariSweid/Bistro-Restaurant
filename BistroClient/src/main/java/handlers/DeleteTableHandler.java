package handlers;

import Controllers.TablesController;
import common.ServerResponse;

public class DeleteTableHandler implements ResponseHandler {

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
