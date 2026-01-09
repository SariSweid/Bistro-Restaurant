package Entities;

import java.io.Serializable;
import java.time.LocalTime;

public class TimeData implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalTime reservationTime;
    private LocalTime actualArrivalTime;
    private LocalTime departureTime;

    public TimeData(LocalTime reservationTime, LocalTime actualArrivalTime, LocalTime departureTime) {
        this.reservationTime = reservationTime;
        this.actualArrivalTime = actualArrivalTime;
        this.departureTime = departureTime;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public LocalTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    @Override
    public String toString() {
        return "TimeData [reservationTime=" + reservationTime + 
               ", actualArrivalTime=" + actualArrivalTime + 
               ", departureTime=" + departureTime + "]";
    }
}
