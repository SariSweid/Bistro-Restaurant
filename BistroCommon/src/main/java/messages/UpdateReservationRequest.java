package messages;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;


import Entities.Reservation.Status;




public class UpdateReservationRequest implements Serializable {

    private int reservationID;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numOfGuests;
    private Status status;

    public UpdateReservationRequest(int reservationID,LocalDate reservationDate, LocalTime reservationTime,int numOfGuests,Status status) {
    	
        this.reservationID = reservationID;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.numOfGuests = numOfGuests;
        this.status = status;
        
    }

    public int getReservationID() { return reservationID; }
    public LocalDate getReservationDate() { return reservationDate; }
    public LocalTime getReservationTime() { return reservationTime; }
    public int getNumOfGuests() { return numOfGuests; }
    public Status getStatus() { return status; }
}
