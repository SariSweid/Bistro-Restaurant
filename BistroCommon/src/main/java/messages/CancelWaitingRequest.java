package messages;

import java.io.Serializable;

public class CancelWaitingRequest implements Serializable {
    private int confirmationCode;

    public CancelWaitingRequest(int confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public int getConfirmationCode() { return confirmationCode; }
}
