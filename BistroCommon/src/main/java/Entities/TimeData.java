package Entities;

import java.io.Serializable;
import java.time.LocalTime;

public class TimeData implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalTime arrivalTime;
    private LocalTime departureTime;

    public TimeData(LocalTime arrivalTime, LocalTime departureTime) {
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

	@Override
	public String toString() {
		return "TimeData [arrivalTime=" + arrivalTime + ", departureTime=" + departureTime + "]";
	}
}
