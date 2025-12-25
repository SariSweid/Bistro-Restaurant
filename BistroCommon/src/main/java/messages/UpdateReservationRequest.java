package messages;

import java.io.Serializable;
import java.time.LocalDate;

public class UpdateReservationRequest implements Serializable {

    private int reservationID;
    private LocalDate reservationDate;
    private int numOfGuests;

    public UpdateReservationRequest(int ID, LocalDate date, int guests) {
        this.reservationID = ID;
        this.reservationDate = date;
        this.numOfGuests = guests;
    }

    public int getReservationID() { return reservationID; }
    public LocalDate getReservationDate() { return reservationDate; }
    public int getNumOfGuests() { return numOfGuests; }
}
