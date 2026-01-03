package Entities;

import java.io.Serializable;
import java.time.LocalTime;

import enums.Day;
/**
 * WeeklyOpeningHours class represents the opening and closing hours in a normal weekday
 */
public class WeeklyOpeningHours extends OpeningHours implements Serializable {
	private Day day;
	
	/**
	 * constructor for a new opening and closing hours of a weekday
	 * @param openingTime
	 * @param closingTime
	 * @param day
	 */
	public WeeklyOpeningHours(LocalTime openingTime, LocalTime closingTime, Day day) {
		super(openingTime, closingTime);
		this.day = day;
	}
	
	//getters
	
	public Day getDay() {
		return this.day;
	}
	
	//setters
	
	public void setDay(Day newDay) {
		this.day = newDay;
	}
}
