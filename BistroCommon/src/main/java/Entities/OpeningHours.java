package Entities;

import java.time.LocalTime;

/**
 * base class for opening hours
 */
public abstract class OpeningHours {
	private LocalTime openingTime;
	private LocalTime closingTime;
	
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
