package Entities;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * base class for opening hours, used for WeeklyOpeningHours and SpecialDates
 */
public abstract class OpeningHours implements Serializable {
	private LocalTime openingTime;
	private LocalTime closingTime;
	
	/**
	 * constructor for setting opening time and closing time of the restaurant
	 * @param openingTime
	 * @param closingTime
	 */
	protected OpeningHours(LocalTime openingTime, LocalTime closingTime) {
		this.openingTime = openingTime;
		this.closingTime = closingTime;
	}
	
	//getters
	
	public LocalTime getOpeningTime() {
		return this.openingTime;
	}
	
	public LocalTime getClosingTime() {
		return this.closingTime;
	}
	
	//setters
	
	public void setOpeningTime(LocalTime newOpeningTime) {
		this.openingTime = newOpeningTime;
	}
	
	public void setClosingTime(LocalTime newClosingTime) {
		this.closingTime = newClosingTime;
	}
}
