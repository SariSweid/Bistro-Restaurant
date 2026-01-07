package messages;

import java.io.Serializable;

public class SeatCustomerRequest implements Serializable {

    private int userId;
    private int confirmationCode;

    public SeatCustomerRequest(int userId, int confirmationCode) {
        this.userId = userId;
        this.confirmationCode = confirmationCode;
    }

    public int getUserId() {
        return userId;
    }

    public int getConfirmationCode() {
        return confirmationCode;
    }
}
