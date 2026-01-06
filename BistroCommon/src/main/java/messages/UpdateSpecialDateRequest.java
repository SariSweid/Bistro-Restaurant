package messages;


import java.io.Serializable;

import java.time.LocalDate;
import java.time.LocalTime;


public class UpdateSpecialDateRequest implements Serializable{
	private String description;
    private LocalDate date;
    private LocalTime openingTime;
    private LocalTime closingTime;
    
    public UpdateSpecialDateRequest(String description,LocalDate date, LocalTime openingTime,LocalTime closingTime) {
    	
        this.description = description;
        this.date = date;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
    
    public LocalTime getClosingTime() {
		return closingTime;
	}
    
    public LocalDate getDate() {
		return date;
	}
    
    public LocalTime getOpeningTime() {
		return openingTime;	
    }
    
    public String getDescription() {
		return description;
	}
}
