package messages;

import java.io.Serializable;

public class InsertTableRequest implements Serializable {
    private int tableId;
    private int seats;

    public InsertTableRequest(int tableId, int seats) {
        this.tableId = tableId;
        this.seats = seats;
    }

    public int getTableId() { return tableId; }
    public int getSeats() { return seats; }
}
