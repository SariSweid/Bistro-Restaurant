package Entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * RestaurantSettings class represents the restaurant configuration which include special dates, max tables and opening and closing hours
 * singleton - in order to create a single instance in the system
 */
public class RestaurantSettings {
	private static RestaurantSettings instance;
	private LocalTime openingTime;
	private LocalTime closingTime;
	private int maxTables;
	private List<SpecialDates> specialDates;
	
	private RestaurantSettings() {
		this.specialDates = new ArrayList<SpecialDates>();
	}
	
	public static RestaurantSettings getInstance() {
		if(instance == null) {
			instance = new RestaurantSettings();
		}
		return instance;
	}
	
	//getters
	
	public LocalTime getOpeningTime() {
		return this.openingTime;
	}
	
	public LocalTime getClosingTime() {
		return this.closingTime;
	}
	
	public int getMaxTables() {
		return this.maxTables;
	}
	
	public List<SpecialDates> getSpecialDates(){
		return this.specialDates;
	}
	
	//setters
	
	public void setOpeningTime(LocalTime newOpeningTime) {
		this.openingTime = newOpeningTime;
	}
	
	public void setClosingTime(LocalTime newClosingTime) {
		this.closingTime = newClosingTime;
	}
	
	public void setMaxTables(int newMaxTables) {
		this.maxTables = newMaxTables;
	}
	
	public void addSpecialDate(SpecialDates specialDate) {
		this.specialDates.add(specialDate);
	}
}
