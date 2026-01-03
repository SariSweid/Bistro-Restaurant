package Entities;

import java.time.LocalTime;

import enums.Day;

public class WeeklyOpeningHours extends OpeningHours{
	private Day day;
	
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
