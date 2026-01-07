package messages;

import java.io.Serializable;
import java.time.LocalTime;

public class updateRegularClosingTimeRequest implements Serializable {

    private LocalTime closingTime;

    public updateRegularClosingTimeRequest(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }//
}
