package messages;

import Entities.Reservation;

public class AddReservationRequest {
    private Reservation reservation;

    public AddReservationRequest(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}