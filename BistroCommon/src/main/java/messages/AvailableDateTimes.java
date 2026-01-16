package messages;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Represents available reservation times for a specific date.
 * Contains a date and a list of times when reservations can be made.
 */
@SuppressWarnings("serial")
public class AvailableDateTimes implements Serializable {

    private final LocalDate date;
    private final List<LocalTime> times;

    /**
     * Constructs a new AvailableDateTimes object with the specified date and times.
     *
     * @param date  the date for which the times are available
     * @param times a list of available times for the given date
     */
    public AvailableDateTimes(LocalDate date, List<LocalTime> times) {
        this.date = date;
        this.times = times;
    }

    /**
     * Returns the date for which these times are available.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the list of available times for the date.
     *
     * @return a list of LocalTime objects
     */
    public List<LocalTime> getTimes() {
        return times;
    }
}
