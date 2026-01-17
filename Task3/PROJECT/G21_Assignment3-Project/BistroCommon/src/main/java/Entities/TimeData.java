package Entities;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Represents time-related data for a reservation.
 * Stores the reservation time, actual arrival time, and departure time.
 */
public class TimeData implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalTime reservationTime;
    private LocalTime actualArrivalTime;
    private LocalTime departureTime;

    /**
     * Constructs a new TimeData object with the specified times.
     * 
     * @param reservationTime the reserved time for the reservation
     * @param actualArrivalTime the actual arrival time of the customer
     * @param departureTime the departure time of the customer
     */
    public TimeData(LocalTime reservationTime, LocalTime actualArrivalTime, LocalTime departureTime) {
        this.reservationTime = reservationTime;
        this.actualArrivalTime = actualArrivalTime;
        this.departureTime = departureTime;
    }

    /**
     * Returns the reserved time for the reservation.
     * 
     * @return the reservation time
     */
    public LocalTime getReservationTime() {
        return reservationTime;
    }

    /**
     * Returns the actual arrival time of the customer.
     * 
     * @return the actual arrival time
     */
    public LocalTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    /**
     * Returns the departure time of the customer.
     * 
     * @return the departure time
     */
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Returns a string representation of the TimeData object.
     * Includes reservation time, actual arrival time, and departure time.
     * 
     * @return a string representation of this TimeData
     */
    @Override
    public String toString() {
        return "TimeData [reservationTime=" + reservationTime + 
               ", actualArrivalTime=" + actualArrivalTime + 
               ", departureTime=" + departureTime + "]";
    }
}
