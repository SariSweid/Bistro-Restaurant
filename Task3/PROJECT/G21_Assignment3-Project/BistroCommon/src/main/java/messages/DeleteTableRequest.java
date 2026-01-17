package messages;

import java.io.Serializable;

/**
 * Represents a request message to delete a table from the restaurant system.
 * Contains the unique ID of the table to be removed.
 */
@SuppressWarnings("serial")
public class DeleteTableRequest implements Serializable {

    /**
     * The unique identifier (ID) of the table to delete.
     */
    private int tableId;

    /**
     * Constructs a new DeleteTableRequest with the specified table ID.
     *
     * @param tableId the ID of the table to remove
     */
    public DeleteTableRequest(int tableId) {
        this.tableId = tableId;
    }

    /**
     * Returns the table ID associated with this request.
     *
     * @return the ID of the table
     */
    public int getTableId() {
        return tableId;
    }
}