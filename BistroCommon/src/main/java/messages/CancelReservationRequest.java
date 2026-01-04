package messages;

import java.io.Serializable;

public class CancelReservationRequest implements Serializable {
    private int reservationId;

    public CancelReservationRequest(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getReservationId() {
        return reservationId;
    }
}
