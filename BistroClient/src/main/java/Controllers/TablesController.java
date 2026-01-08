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
        reloadTables();
    }

    @FXML
    private void onInsert() {
        Integer id = parseIntegerField(tableIdField, "Table ID");
        Integer seats = parseIntegerField(seatsField, "Seats");
        if (id == null || seats == null) return;

        // Check if ID already exists in table list
        boolean exists = tablesTable.getItems().stream()
            .anyMatch(t -> t.getTableID() == id);

        if (exists) {
            showError("Table ID already exists! Choose another.");
            return;
        }

        try {
            ClientHandler.getClient().insertTable(id, seats);
            showInfo("Table inserted successfully!");
            reloadTables();
        } catch (Exception e) {
            showError("Failed to insert table: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        Integer id = parseIntegerField(tableIdField, "Table ID");
        Integer seats = parseIntegerField(seatsField, "Seats");
        if (id == null || seats == null) return;

        // Check if the table exists
        boolean exists = tablesTable.getItems().stream()
            .anyMatch(t -> t.getTableID() == id);

        if (!exists) {
            showError("Table ID does not exist!");
            return;
        }

        try {
            ClientHandler.getClient().updateTable(id, seats);
            showInfo("Table updated successfully!");
            reloadTables();
        } catch (Exception e) {
            showError("Failed to update table: " + e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        Integer id = parseIntegerField(tableIdField, "Table ID");
        if (id == null) return;

        // Check if the table exists
        boolean exists = tablesTable.getItems().stream()
            .anyMatch(t -> t.getTableID() == id);

        if (!exists) {
            showError("Table ID does not exist!");
            return;
        }

        try {
            ClientHandler.getClient().deleteTable(id);
            showInfo("Table deleted successfully!");
            reloadTables();
        } catch (Exception e) {
            showError("Failed to delete table: " + e.getMessage());
        }
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
        try {
            ClientHandler.getClient().getAllTables();
        } catch (Exception e) {
            showError("Failed to load tables: " + e.getMessage());
        }
    }

    // Parse integer fields safely
    private Integer parseIntegerField(TextField field, String fieldName) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            showError(fieldName + " must not be empty!");
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showError(fieldName + " must be a number!");
            return null;
        }
    }

    // Alerts
    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
