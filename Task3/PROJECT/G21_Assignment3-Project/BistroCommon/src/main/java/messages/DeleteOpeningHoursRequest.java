package messages;

import enums.Day;
import java.io.Serializable;

/**
 * Represents a request to delete the opening hours for a specific day
 * in the restaurant's schedule.
 */
@SuppressWarnings("serial")
public class DeleteOpeningHoursRequest implements Serializable {

    private final Day day;

    /**
     * Constructs a new request to delete opening hours for the specified day.
     *
     * @param day the day for which the opening hours should be deleted
     */
    public DeleteOpeningHoursRequest(Day day) {
        this.day = day;
    }

    /**
     * Returns the day for which the opening hours deletion is requested.
     *
     * @return the day
     */
    public Day getDay() {
        return day;
    }
}
