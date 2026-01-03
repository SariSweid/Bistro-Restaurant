package messages;

import java.io.Serializable;
import java.time.LocalDate;

public class GetAvailableTimesRequest implements Serializable {
    private final LocalDate date;
    private final int guests;

    public GetAvailableTimesRequest(LocalDate date, int guests) {
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
