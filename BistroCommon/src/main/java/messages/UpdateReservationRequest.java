package messages;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


import enums.ReservationStatus;




@SuppressWarnings("serial")
public class UpdateReservationRequest implements Serializable {

    private int reservationID;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numOfGuests;
    private ReservationStatus status;

    public UpdateReservationRequest(int reservationID,LocalDate reservationDate, LocalTime reservationTime,int numOfGuests,ReservationStatus status) {
    	
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
    public ReservationStatus getStatus() { return status; }
}
