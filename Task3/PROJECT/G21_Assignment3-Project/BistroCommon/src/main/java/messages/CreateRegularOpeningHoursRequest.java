package messages;

import Entities.WeeklyOpeningHours;
import java.io.Serializable;

/**
 * Represents a request to create regular opening hours for a specific weekday.
 * Encapsulates a WeeklyOpeningHours object containing the day and opening/closing times.
 */
@SuppressWarnings("serial")
public class CreateRegularOpeningHoursRequest implements Serializable {

    private WeeklyOpeningHours hours;

    /**
     * Constructs a new CreateRegularOpeningHoursRequest with the specified opening hours.
     * 
     * @param hours the WeeklyOpeningHours object to be created
     */
    public CreateRegularOpeningHoursRequest(WeeklyOpeningHours hours) {
        this.hours = hours;
    }

    /**
     * Returns the WeeklyOpeningHours object contained in this request.
     * 
     * @return the WeeklyOpeningHours to be created
     */
    public WeeklyOpeningHours getHours() {
        return hours;
    }
}
