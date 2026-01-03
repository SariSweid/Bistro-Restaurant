package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * SpecialDates class represents a special date with different opening and closing hours for the restaurant
 */
public class SpecialDates extends OpeningHours implements Serializable {
	private LocalDate date;
	private String description;
	
	/**
	 * constructor for new special date
	 * @param openingTime
	 * @param closingTime
	 * @param date
	 * @param description
	 */
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
