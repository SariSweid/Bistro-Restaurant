package Entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class SpecialDates extends OpeningHours{
	private LocalDate date;
	private String description;
	
	public SpecialDates(LocalTime openingTime, LocalTime closingTime, LocalDate date, String description) {
		super(openingTime, closingTime);
		this.date = date;
		this.description = description;
	}
	
	//getters
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	//setters
	
	public void setDate(LocalDate newDate) {
		this.date = newDate;
	}
	
	public void setDescription(String newDescription) {
		this.description = newDescription;
	}
}
