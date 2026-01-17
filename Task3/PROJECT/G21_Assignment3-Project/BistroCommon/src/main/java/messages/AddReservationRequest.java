package messages;

import java.io.Serializable;
import Entities.Reservation;

/**
 * Represents a request to add a new reservation in the system.
 * Encapsulates a {@link Reservation} object to be sent to the server.
 */
@SuppressWarnings("serial")
public class AddReservationRequest implements Serializable {

    private Reservation reservation;

    /**
     * Constructs a new request with the specified reservation.
     *
     * @param reservation the reservation to be added
     */
    public AddReservationRequest(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * Returns the reservation associated with this request.
     *
     * @return the reservation object
     */
    public Reservation getReservation() {
        return reservation;
    }
}
