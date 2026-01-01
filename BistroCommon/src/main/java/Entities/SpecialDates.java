package Entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class SpecialDates {
	private LocalDate date;
	private LocalTime openingTime;
	private LocalTime closingTime;
	private String description;
	
	public SpecialDates(LocalDate date, LocalTime openingTime, LocalTime closingTime, String description) {
		this.date = date;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
		this.description = description;
	}
	
	//getters
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public LocalTime getOpeningTime() {
		return this.openingTime;
	}
	
	public LocalTime getClosingTime() {
		return this.closingTime;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	//setters
	
	public void setDate(LocalDate newDate) {
		this.date = newDate;
	}
	
	public void setOpeningTime(LocalTime newOpeningTime) {
		this.openingTime = newOpeningTime;
	}
	
	public void setClosingTime(LocalTime newClosingTime) {
		this.closingTime = newClosingTime;
	}
	
	public void setDescription(String newDescription) {
		this.description = newDescription;
	}
}
