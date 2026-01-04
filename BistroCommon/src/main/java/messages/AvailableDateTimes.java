package messages;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AvailableDateTimes implements Serializable {

    private final LocalDate date;
    private final List<LocalTime> times;

    public AvailableDateTimes(LocalDate date, List<LocalTime> times) {
        this.date = date;
        this.times = times;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<LocalTime> getTimes() {
        return times;
    }
}
