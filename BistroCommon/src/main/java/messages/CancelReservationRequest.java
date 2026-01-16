package messages;

import java.io.Serializable;

/**
 * Represents a request to cancel a reservation in the restaurant system.
 * Can be used by subscribers or guests, depending on which identifiers are provided.
 */
@SuppressWarnings("serial")
public class CancelReservationRequest implements Serializable {

    /**
     * The reservation ID used by subscribers to cancel their reservation.
     */
    private Integer reservationId;

    /**
     * The confirmation code used by guests to cancel their reservation.
     */
    private Integer confirmationCode;

    /**
     * The guest ID associated with the reservation (used for guests).
     */
    private Integer guestId;

    /**
     * Constructs a new CancelReservationRequest with the given identifiers.
     *
     * @param reservationId     the reservation ID (for subscribers)
     * @param confirmationCode  the confirmation code (for guests)
     * @param guestId           the guest ID (for guests)
     */
    public CancelReservationRequest(Integer reservationId, Integer confirmationCode, Integer guestId) {
        this.reservationId = reservationId;
        this.confirmationCode = confirmationCode;
        this.guestId = guestId;
    }

    /**
     * Returns the reservation ID.
     *
     * @return the reservation ID
     */
    public Integer getReservationId() {
        return reservationId;
    }

    /**
     * Returns the confirmation code.
     *
     * @return the confirmation code
     */
    public Integer getConfirmationCode() {
        return confirmationCode;
    }

    /**
     * Returns the guest ID associated with the reservation.
     *
     * @return the guest ID
     */
    public Integer getGuestId() {
        return guestId;
    }
}
