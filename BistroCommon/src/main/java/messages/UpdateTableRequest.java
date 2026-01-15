package messages;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UpdateTableRequest implements Serializable {
    private int tableId;
    private int seats;

    public UpdateTableRequest(int tableId, int seats) {
        this.tableId = tableId;
        this.seats = seats;
    }

    public int getTableId() { return tableId; }
    public int getSeats() { return seats; }
}
