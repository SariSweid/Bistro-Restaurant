package messages;

import enums.Day;
import java.io.Serializable;
import java.time.LocalTime;

@SuppressWarnings("serial")
public class updateRegularOpeningTimeRequest implements Serializable {
    private Day day;
    private LocalTime openingTime;

    public updateRegularOpeningTimeRequest(Day day, LocalTime openingTime) {
        this.day = day;
        this.openingTime = openingTime;
    }

    public Day getDay() { return day; }
    public LocalTime getOpeningTime() { return openingTime; }
}
