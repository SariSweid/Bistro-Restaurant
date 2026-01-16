package logicControllers;

import DAO.TableDAO;

/**
 * Controller responsible for the administrative configuration of restaurant tables.
 * This class handles CRUD operations (Create, Update, Delete) for the table infrastructure,
 * allowing managers to modify the restaurant's physical layout in the database.
 */
public class TableSettingsController {

    private final TableDAO db = new TableDAO();

    /**
     * Adds a new table to the restaurant system.
     * * @param id    The unique identifier (number) for the new table.
     * @param seats The maximum seating capacity for this table.
     * @return true if the table was successfully added to the database, false otherwise.
     */
    public boolean insertTable(int id, int seats) {
        try {
            return db.insertTable(id, seats);
        } catch (Exception e) {
            // Logs the error to console for debugging purposes
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the configuration of an existing table.
     * Typically used to change the seating capacity of a specific table.
     * * @param id    The unique identifier of the table to be updated.
     * @param seats The new maximum seating capacity.
     * @return true if the record was successfully updated, false otherwise.
     */
    public boolean updateTable(int id, int seats) {
        try {
            return db.updateTable(id, seats);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes a table from the restaurant system.
     * Note: Should be used with caution, as tables might be linked to historical reservation data.
     * * @param id The unique identifier of the table to be deleted.
     * @return true if the table was successfully removed, false otherwise.
     */
    public boolean deleteTable(int id) {
        try {
            return db.deleteTable(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}