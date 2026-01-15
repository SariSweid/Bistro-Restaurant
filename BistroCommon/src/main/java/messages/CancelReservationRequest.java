package messages;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CancelReservationRequest implements Serializable {
	private Integer reservationId;       // For Subscriber
    private Integer confirmationCode;    // For Guest
    private Integer guestId; // for guests

    public CancelReservationRequest(Integer reservationId, Integer confirmationCode, Integer guestId) {
        this.reservationId = reservationId;
        this.confirmationCode = confirmationCode;
        this.guestId = guestId;
    }

    public Integer getReservationId() { return reservationId; }
    public Integer getConfirmationCode() { return confirmationCode; }
    public Integer getGuestId() { return guestId; }
}