package messages;
import java.io.Serializable;
import java.time.LocalTime;

import enums.Day;

public class updateRegularOpeningTimeRequest implements Serializable {
	private Day day;
	private LocalTime openingTime;
	
	public updateRegularOpeningTimeRequest(Day day, LocalTime openingTime) {
		this.day=day;
		this.openingTime = openingTime;
	}
	
	public Day getDay() {
		return day;
	}
	
	public LocalTime getOpeningTime() {
		return openingTime;
	}
}
