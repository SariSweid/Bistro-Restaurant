package messages;

import java.io.Serializable;

/**
 * Represents a request message to update the details of an existing table in the restaurant.
 * This request is primarily used to change the number of seats assigned to a specific table ID.
 */
@SuppressWarnings("serial")
public class UpdateTableRequest implements Serializable {

    /**
     * The unique identifier (ID) of the table to be updated.
     */
    private int tableId;

    /**
     * The new number of seats to be assigned to the table.
     */
    private int seats;

    /**
     * Constructs a new UpdateTableRequest with the specified table ID and seat count.
     *
     * @param tableId the unique ID of the table
     * @param seats   the new number of seats for this table
     */
    public UpdateTableRequest(int tableId, int seats) {
        this.tableId = tableId;
        this.seats = seats;
    }

    /**
     * Returns the table ID associated with this request.
     *
     * @return the unique identifier of the table
     */
    public int getTableId() {
        return tableId;
    }

    /**
     * Returns the new number of seats associated with this request.
     *
     * @return the number of seats
     */
    public int getSeats() {
        return seats;
    }
}