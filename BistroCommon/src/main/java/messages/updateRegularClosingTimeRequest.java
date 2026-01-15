package messages;

import enums.Day;
import java.io.Serializable;
import java.time.LocalTime;

@SuppressWarnings("serial")
public class updateRegularClosingTimeRequest implements Serializable {
    private Day day;
    private LocalTime closingTime;

    public updateRegularClosingTimeRequest(Day day, LocalTime closingTime) {
        this.day = day;
        this.closingTime = closingTime;
    }

    public Day getDay() { return day; }
    public LocalTime getClosingTime() { return closingTime; }
}

