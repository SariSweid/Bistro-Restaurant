package messages;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DeleteTableRequest implements Serializable {
    private int tableId;

    public DeleteTableRequest(int tableId) {
        this.tableId = tableId;
    }

    public int getTableId() { return tableId; }
}
