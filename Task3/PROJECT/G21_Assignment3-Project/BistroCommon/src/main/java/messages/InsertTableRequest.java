package messages;

import java.io.Serializable;

/**
 * Represents a request message to insert a new table into the restaurant system.
 * This request provides the necessary details to register a physical table, 
 * including its unique identifier and its seating capacity.
 */
@SuppressWarnings("serial")
public class InsertTableRequest implements Serializable {

    /**
     * The unique identifier (ID) to be assigned to the new table.
     */
    private int tableId;

    /**
     * The maximum number of seats (capacity) for the new table.
     */
    private int seats;

    /**
     * Constructs a new InsertTableRequest with the specified table ID and seat count.
     *
     * @param tableId the unique ID for the new table
     * @param seats   the number of seats available at this table
     */
    public InsertTableRequest(int tableId, int seats) {
        this.tableId = tableId;
        this.seats = seats;
    }

    /**
     * Returns the table ID associated with this insertion request.
     *
     * @return the unique identifier of the table
     */
    public int getTableId() {
        return tableId;
    }

    /**
     * Returns the number of seats for the table to be inserted.
     *
     * @return the seat capacity
     */
    public int getSeats() {
        return seats;
    }
}