package messages;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class GetNearestAvailableTimesRequest implements Serializable {

    private final LocalDate date;
    private final int guests;

    public GetNearestAvailableTimesRequest(LocalDate date, int guests) {
        this.date = date;
        this.guests = guests;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getGuests() {
        return guests;
    }
}
