package messages;

import Entities.WeeklyOpeningHours;
import java.io.Serializable;

public class CreateRegularOpeningHoursRequest implements Serializable {
    private WeeklyOpeningHours hours;

    public CreateRegularOpeningHoursRequest(WeeklyOpeningHours hours) {
        this.hours = hours;
    }

    public WeeklyOpeningHours getHours() {
        return hours;
    }
}
