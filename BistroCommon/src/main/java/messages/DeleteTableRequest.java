package messages;

import java.io.Serializable;

public class DeleteTableRequest implements Serializable {
    private int tableId;

    public DeleteTableRequest(int tableId) {
        this.tableId = tableId;
    }

    public int getTableId() { return tableId; }
}
