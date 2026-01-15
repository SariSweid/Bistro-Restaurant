package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import enums.Day;

/**
 * RestaurantSettings class represents the restaurant configuration which include special dates, max tables and opening and closing hours
 * singleton - in order to create a single instance in the system
 */
@SuppressWarnings("serial")
public class RestaurantSettings implements Serializable {
	private static RestaurantSettings instance;
	private int maxTables;
	private List<WeeklyOpeningHours> weeklyOpeningHours;
	private List<SpecialDates> specialDates;
	
	/**
	 * private constructor in order to access only from getInstance() method
	 */
	private RestaurantSettings() {
		this.weeklyOpeningHours = new ArrayList<WeeklyOpeningHours>();
		this.specialDates = new ArrayList<SpecialDates>();
	}
	
	public void setWeeklyOpeningHours(List<WeeklyOpeningHours> weeklyOpeningHours) {
		this.weeklyOpeningHours = weeklyOpeningHours;
	}

	public void setSpecialDates(List<SpecialDates> specialDates) {
		this.specialDates = specialDates;
	}

	/**
	 * getInstance returns new instance of RestaurantSettings if not created yet, if created returns the instance
	 * @return the only instance of RestaurantSettings
	 */
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
	
	/**
	 * getOpeningHoursForDay returns the opening and closing hours based with given day as input
	 * @param day
	 * @return the opening and closing hours based on the day given in the method
	 */
	public WeeklyOpeningHours getOpeningHoursForDay(Day day) {
		return this.weeklyOpeningHours.stream().filter(e -> e.getDay() == day).findFirst().orElse(null);
	}
	
	/**
	 * addWeeklyOpeningHour add a weekday opening and closing hours to the list of weekly dates
	 * @param hours
	 */
	public void addWeeklyOpeningHour(WeeklyOpeningHours hours) {
		this.weeklyOpeningHours.add(hours);
	}
	
	/**
	 * addSpecialDate add a special date to the list of special dates
	 * @param specialDate
	 */
	public void addSpecialDate(SpecialDates specialDate) {
		this.specialDates.add(specialDate);
	}

	
	private int reservationDurationHours = 2; // default

	public int getReservationDurationHours() {
	    return reservationDurationHours;
	}
	
	public void setReservationDurationHours(int hours) {
	    this.reservationDurationHours = hours;
	}
	
	
	
	
	/**
	 * Returns the opening hours for a specific date.
	 * Special dates override weekly hours.
	 */
	public WeeklyOpeningHours getOpeningHoursForDate(java.time.LocalDate date) {

	    // Check special dates override
	    if (specialDates != null) {
	        for (SpecialDates sp : specialDates) {
	            if (sp.getDate().equals(date)) {
	                return new WeeklyOpeningHours(
	                    sp.getOpeningTime(),
	                    sp.getClosingTime(),
	                    Day.valueOf(date.getDayOfWeek().name())
	                );
	            }
	        }
	    }

	    // Fall back to weekly hours
	    if (weeklyOpeningHours != null) {
	        Day day = Day.valueOf(date.getDayOfWeek().name());
	        for (WeeklyOpeningHours wh : weeklyOpeningHours) {
	            if (wh.getDay() == day) {
	                return wh;
	            }
	        }
	    }

	    // No hours = restaurant closed
	    return null;
	}


}
