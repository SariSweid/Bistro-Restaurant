package messages;

import java.io.Serializable;


import Entities.Reservation;

@SuppressWarnings("serial")
public class AddReservationRequest implements Serializable {
    private Reservation reservation;

    public AddReservationRequest(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}