package messages;

import enums.Day;
import java.io.Serializable;

@SuppressWarnings("serial")
public class DeleteOpeningHoursRequest implements Serializable {
    private final Day day;

    public DeleteOpeningHoursRequest(Day day) {
        this.day = day;
    }

    public Day getDay() {
        return day;
    }
}
