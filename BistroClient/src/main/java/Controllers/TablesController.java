package Controllers;

import java.util.List;

import Entities.Table;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import util.SceneManager;

public class TablesController {

    @FXML private TableView<Table> tablesTable;
    @FXML private TableColumn<Table, Integer> tableIdColumn;
    @FXML private TableColumn<Table, Integer> seatsColumn;

    @FXML private TextField tableIdField;
    @FXML private TextField seatsField;

    @FXML
    public void initialize() {

        // Setup table columns
        tableIdColumn.setCellValueFactory(new PropertyValueFactory<>("tableID"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        // Register this controller in ClientHandler
        ClientHandler.getClient().setTablesController(this);

        // Load tables from server
        ClientHandler.getClient().getAllTables();
    }

    @FXML
    private void onInsert() {
        int id = Integer.parseInt(tableIdField.getText());
        int seats = Integer.parseInt(seatsField.getText());

        ClientHandler.getClient().insertTable(id, seats);
    }

    @FXML
    private void onUpdate() {
        int id = Integer.parseInt(tableIdField.getText());
        int seats = Integer.parseInt(seatsField.getText());

        ClientHandler.getClient().updateTable(id, seats);
    }

    @FXML
    private void onDelete() {
        int id = Integer.parseInt(tableIdField.getText());

        ClientHandler.getClient().deleteTable(id);
    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }

    // Called by handlers
    public void updateTableList(List<Table> tables) {
        tablesTable.getItems().setAll(tables);
    }

    public void reloadTables() {
        ClientHandler.getClient().getAllTables();
    }

    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    public void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}

