package messages;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import enums.ReservationStatus;

/**
 * Represents a request to update an existing reservation in the system.
 * This class carries the updated details such as date, time, number of guests, and status.
 */
@SuppressWarnings("serial")
public class UpdateReservationRequest implements Serializable {

    /** The unique identifier of the reservation to be updated. */
    private int reservationID;
    
    /** The updated date for the reservation. */
    private LocalDate reservationDate;
    
    /** The updated time for the reservation. */
    private LocalTime reservationTime;
    
    /** The updated number of guests for the reservation. */
    private int numOfGuests;
    
    /** The updated status of the reservation (e.g., Confirmed, Cancelled). */
    private ReservationStatus status;

    /**
     * Constructs a new UpdateReservationRequest with the specified details.
     * * @param reservationID   the unique ID of the reservation
     * @param reservationDate the new date for the reservation
     * @param reservationTime the new time for the reservation
     * @param numOfGuests     the updated number of guests
     * @param status          the updated {@link ReservationStatus}
     */
    public UpdateReservationRequest(int reservationID, LocalDate reservationDate, LocalTime reservationTime, int numOfGuests, ReservationStatus status) {
    	
        this.reservationID = reservationID;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.numOfGuests = numOfGuests;
        this.status = status;
        
    }

    /**
     * Gets the reservation ID.
     * @return the reservationID
     */
    public int getReservationID() { return reservationID; }

    /**
     * Gets the scheduled date of the reservation.
     * @return the reservationDate
     */
    public LocalDate getReservationDate() { return reservationDate; }

    /**
     * Gets the scheduled time of the reservation.
     * @return the reservationTime
     */
    public LocalTime getReservationTime() { return reservationTime; }

    /**
     * Gets the number of guests.
     * @return the numOfGuests
     */
    public int getNumOfGuests() { return numOfGuests; }

    /**
     * Gets the current status of the reservation update.
     * @return the {@link ReservationStatus}
     */
    public ReservationStatus getStatus() { return status; }
}