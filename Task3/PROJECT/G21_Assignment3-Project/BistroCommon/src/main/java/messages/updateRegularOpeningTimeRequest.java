package messages;

import enums.Day;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * Represents a request message to update the standard opening time for a specific day of the week.
 * This is used to modify the restaurant's recurring weekly schedule.
 */
@SuppressWarnings("serial")
public class updateRegularOpeningTimeRequest implements Serializable {

    /**
     * The day of the week for which the opening time is being updated.
     */
    private Day day;

    /**
     * The new standard opening time to be applied for the specified day.
     */
    private LocalTime openingTime;

    /**
     * Constructs a new updateRegularOpeningTimeRequest with the specified day and opening time.
     *
     * @param day         the Day of the week to update
     * @param openingTime the new LocalTime for opening
     */
    public updateRegularOpeningTimeRequest(Day day, LocalTime openingTime) {
        this.day = day;
        this.openingTime = openingTime;
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
     * Returns the new opening time associated with this request.
     *
     * @return the LocalTime object representing the new opening hour
     */
    public LocalTime getOpeningTime() {
        return openingTime;
    }
}