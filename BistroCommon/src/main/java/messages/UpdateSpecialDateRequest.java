package messages;


import java.io.Serializable;

import java.time.LocalDate;
import java.time.LocalTime;


public class UpdateSpecialDateRequest implements Serializable{
	
	private LocalDate oldDate; //the date of the selected
	 
	private String description;
    private LocalDate date;
    private LocalTime openingTime;
    private LocalTime closingTime;
    
    public UpdateSpecialDateRequest(LocalDate oldDate, String description,LocalDate date, LocalTime openingTime,LocalTime closingTime) {
    	
    	this.oldDate=oldDate;
        this.description = description;
        this.date = date;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
    
    public LocalDate getOldDate() {
    	return oldDate;
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
	}//
}
