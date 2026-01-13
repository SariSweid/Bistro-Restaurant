package messages;

import java.io.Serializable;

public class CancelWaitingRequest implements Serializable {
    private int confirmationCode;
    private Integer userId;

    public CancelWaitingRequest(int confirmationCode, Integer userId) {
        this.confirmationCode = confirmationCode;
        this.userId = userId;
    }

    public int getConfirmationCode() { return confirmationCode; }
    public Integer getUserId() { return userId; }
}
