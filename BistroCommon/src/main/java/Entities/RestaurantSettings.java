package Entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import enums.Day;

/**
 * RestaurantSettings class represents the restaurant configuration which include special dates, max tables and opening and closing hours
 * singleton - in order to create a single instance in the system
 */
public class RestaurantSettings {
	private static RestaurantSettings instance;
	private int maxTables;
	private List<WeeklyOpeningHours> weeklyOpeningHours;
	private List<SpecialDates> specialDates;
	
	private RestaurantSettings() {
		this.weeklyOpeningHours = new ArrayList<WeeklyOpeningHours>();
		this.specialDates = new ArrayList<SpecialDates>();
	}
	
	public static RestaurantSettings getInstance() {
		if(instance == null) {
			instance = new RestaurantSettings();
		}
		return instance;
	}
	
	//getters
	
	public int getMaxTables() {
		return this.maxTables;
	}
	
	public List<WeeklyOpeningHours> getWeeklyOpeningHours(){
		return this.weeklyOpeningHours;
	}
	
	public List<SpecialDates> getSpecialDates(){
		return this.specialDates;
	}
	
	//setters
	
	public void setMaxTables(int newMaxTables) {
		this.maxTables = newMaxTables;
	}
	
	public WeeklyOpeningHours getOpeningHoursForDay(Day day) {
		return this.weeklyOpeningHours.stream().filter(e -> e.getDay() == day).findFirst().orElse(null);
	}
	
	public void addSpecialDate(SpecialDates specialDate) {
		this.specialDates.add(specialDate);
	}
}
