package Controllers;

import java.util.List;

import Entities.Table;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import util.SceneManager;

/**
 * Controller for managing restaurant tables.
 * Handles displaying tables in a TableView, inserting, updating, deleting tables,
 * and navigation to the previous page.
 * Interacts with ClientHandler to perform server-side operations.
 */
public class TablesController {

    @FXML private TableView<Table> tablesTable;
    @FXML private TableColumn<Table, Integer> tableIdColumn;
    @FXML private TableColumn<Table, Integer> seatsColumn;

    @FXML private TextField tableIdField;
    @FXML private TextField seatsField;

    /**
     * Initializes the controller.
     * Sets up TableView columns, registers this controller in ClientHandler,
     * and loads the current list of tables from the server.
     */
    @FXML
    public void initialize() {
        tableIdColumn.setCellValueFactory(new PropertyValueFactory<>("tableID"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        ClientHandler.getClient().setTablesController(this);
        reloadTables();
    }

    /**
     * Handles inserting a new table.
     * Validates input fields, checks for duplicate table IDs,
     * and calls ClientHandler to insert the table.
     */
    @FXML
    private void onInsert() {
        Integer id = parseIntegerField(tableIdField, "Table ID");
        Integer seats = parseIntegerField(seatsField, "Seats");
        if (id == null || seats == null) return;

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

    /**
     * Handles updating an existing table.
     * Validates input fields, checks if the table exists,
     * and calls ClientHandler to update the table.
     */
    @FXML
    private void onUpdate() {
        Integer id = parseIntegerField(tableIdField, "Table ID");
        Integer seats = parseIntegerField(seatsField, "Seats");
        if (id == null || seats == null) return;

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

    /**
     * Handles deleting a table.
     * Validates the table ID field, checks if the table exists,
     * and calls ClientHandler to delete the table.
     */
    @FXML
    private void onDelete() {
        Integer id = parseIntegerField(tableIdField, "Table ID");
        if (id == null) return;

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

    /**
     * Navigates back to the Supervisor UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }

    /**
     * Updates the TableView with the provided list of tables.
     * Called by ClientHandler after fetching tables from the server.
     * @param tables the list of tables to display
     */
    public void updateTableList(List<Table> tables) {
        tablesTable.getItems().setAll(tables);
    }

    /**
     * Reloads the list of tables from the server via ClientHandler.
     */
    public void reloadTables() {
        try {
            ClientHandler.getClient().getAllTables();
        } catch (Exception e) {
            showError("Failed to load tables: " + e.getMessage());
        }
    }

    /**
     * Parses an integer value from a TextField.
     * Shows an error alert if the field is empty or not a valid number.
     * @param field the TextField to parse
     * @param fieldName the name of the field for error messages
     * @return the parsed integer, or null if invalid
     */
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

    /**
     * Displays an error alert with the provided message.
     * @param msg the message to display
     */
    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Displays an information alert with the provided message.
     * @param msg the message to display
     */
    public void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
