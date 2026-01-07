package messages;
import java.io.Serializable;
import java.time.LocalTime;

public class updateRegularOpeningTimeRequest {
	private final LocalTime openingTime;
    public updateRegularOpeningTimeRequest(LocalTime openingTime) {
    	this.openingTime = openingTime;
    	}
    public LocalTime getOpeningTime() {
    	return openingTime; 
    	}//
}
