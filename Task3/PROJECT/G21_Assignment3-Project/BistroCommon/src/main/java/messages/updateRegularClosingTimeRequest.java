package messages;

import enums.Day;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * Represents a request message to update the standard closing time for a specific day of the week.
 * This is used to modify the restaurant's routine schedule rather than a one-time special date.
 */
@SuppressWarnings("serial")
public class updateRegularClosingTimeRequest implements Serializable {

    /**
     * The day of the week for which the closing time is being updated.
     */
    private Day day;

    /**
     * The new standard closing time to be applied.
     */
    private LocalTime closingTime;

    /**
     * Constructs a new updateRegularClosingTimeRequest with the specified day and time.
     *
     * @param day          the Day of the week to update
     * @param closingTime  the new LocalTime for closing
     */
    public updateRegularClosingTimeRequest(Day day, LocalTime closingTime) {
        this.day = day;
        this.closingTime = closingTime;
    }

    /**
     * Returns the day of the week associated with this request.
     *
     * @return the Day enum value
     */
    public Day getDay() {
        return day;
    }

    /**
     * Returns the new closing time associated with this request.
     *
     * @return the LocalTime object
     */
    public LocalTime getClosingTime() {
        return closingTime;
    }
}