package messages;

import java.io.Serializable;
import java.sql.Date;

public class UpdateReservationRequest implements Serializable {

    private int reservationID;
    private Date reservationDate;
    private int numOfGuests;

    public UpdateReservationRequest(int ID, Date date, int guests) {
        this.reservationID = ID;
        this.reservationDate = date;
        this.numOfGuests = guests;
    }

    public int getReservationID() { return reservationID; }
    public Date getReservationDate() { return reservationDate; }
    public int getNumOfGuests() { return numOfGuests; }
}
