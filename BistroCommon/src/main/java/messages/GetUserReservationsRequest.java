package messages;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetUserReservationsRequest implements Serializable {
    private final int customerId;

    public GetUserReservationsRequest(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }
}