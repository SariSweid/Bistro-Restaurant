package handlers;

import java.util.List;

import Controllers.TablesController;
import Entities.Table;
import common.ServerResponse;

public class GetAllTablesHandler implements ResponseHandler {

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
