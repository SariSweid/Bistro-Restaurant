package logicControllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DAO.RestaurantSettingsDAO;
import DAO.SpecialDatesDAO;
import Entities.RestaurantSettings;
import Entities.SpecialDates;
import Entities.WeeklyOpeningHours;
import enums.Day;

/**
 * RestaurantSettingsController manages the restaurant settings
 * and communicates with RestaurantSettingsDAO for database operations.
 */
public class RestaurantSettingsController {
	private final RestaurantSettings restaurantSettings;
	private final RestaurantSettingsDAO dao;
	private final SpecialDatesDAO SPdao;
	
	
	/**
	 * constructor - creates new RestaurantSettingsController
	 */
	public RestaurantSettingsController() {
		this.restaurantSettings = RestaurantSettings.getInstance();
		this.dao = new RestaurantSettingsDAO();
		this.SPdao = new SpecialDatesDAO();
	}
	
	public RestaurantSettings getRestaurantSettings() {
		return restaurantSettings;
	}

	/**
	 * @return max tables in the restaurant
	 */
	public int getMaxTables() {
		return this.restaurantSettings.getMaxTables();
	}
	
	/**
	 * update the max tables in the restaurant
	 * @param newMaxTables
	 * @return true if the update succeeded
	 */
	public boolean updateMaxTables(int newMaxTables) {
		this.restaurantSettings.setMaxTables(newMaxTables);
		return this.dao.updateMaxTable(restaurantSettings);
	}
	
	/**
	 * gets the opening and closing hours of a given day
	 * @param day
	 * @return opening hours of the day
	 */
	public WeeklyOpeningHours getOpeningHoursForDay(Day day) {
		return this.restaurantSettings.getOpeningHoursForDay(day);
	}
	
	/**
	 * Returns the opening hours for a specific date.
	 * Special dates override weekly hours.
	 */
	public WeeklyOpeningHours getOpeningHoursForDate(LocalDate date) {

	    // Check special dates override
	    for (SpecialDates sp : restaurantSettings.getSpecialDates()) {
	        if (sp.getDate().equals(date)) {
	            return new WeeklyOpeningHours(
	                sp.getOpeningTime(),
	                sp.getClosingTime(),
	                Day.valueOf(date.getDayOfWeek().name())
	            );
	        }
	    }

	    // Otherwise use weekly hours
	    Day day = Day.valueOf(date.getDayOfWeek().name());
	    return restaurantSettings.getOpeningHoursForDay(day);
	}

	
	/**
	 * update opening and closing hours of a given weekday
	 * @param hours
	 * @return true if the opening and closing hours were updated in the db
	 */
	public boolean updateWeeklyOpeningHours(WeeklyOpeningHours hours) {
		// remove old hours for this day
		this.restaurantSettings.getWeeklyOpeningHours().removeIf(h -> h.getDay() == hours.getDay());
		// add new hours
		this.restaurantSettings.addWeeklyOpeningHour(hours);
		
		// update in db using DAO
		boolean openingUpdate = this.dao.updateOpeningHours(restaurantSettings, hours.getDay());
		boolean closingUpdate = this.dao.updateClosingHours(restaurantSettings, hours.getDay());
		
		return openingUpdate && closingUpdate;
	}
	
	/**
	 * @return all weekly opening hours
	 */
	public List<WeeklyOpeningHours> getAllWeeklyOpeningHours(){
		List<WeeklyOpeningHours> hoursList = new ArrayList<>();
		hoursList = dao.getAllWeeklyOpeningHours();
		System.out.println(hoursList);
		restaurantSettings.setWeeklyOpeningHours(hoursList);
		return hoursList;
	}
	
	/**
	 * @return all special dates
	 */
	public List<SpecialDates> getAllSpecialDates(){
		List<SpecialDates> specialList = new ArrayList<>();
		specialList = SPdao.getAllSpecialDates();
		System.out.println(specialList);
		restaurantSettings.setSpecialDates(specialList);
		return specialList;
	}
	
	/**
	 * add special date to the restaurant
	 * @param specialDate
	 * @return true if the date was added to the db
	 */
	public boolean addSpecialDate(SpecialDates specialDate) {
		this.restaurantSettings.addSpecialDate(specialDate);
		return this.SPdao.addSpecialDates(specialDate);
	}
	
	/**
	 * update a special date
	 * @param oldDate
	 * @param specialDate
	 * @return true if the special date was updated in the db
	 */
	public boolean updateSpecialDate(LocalDate oldDate, SpecialDates specialDate) {
		boolean flag = this.SPdao.updateSpecialDates(oldDate, specialDate);
		if(flag) {
			this.restaurantSettings.getSpecialDates().removeIf(s -> s.getDate().equals(oldDate));
			this.restaurantSettings.addSpecialDate(specialDate);
		}
		
		return flag;
	}
	
}
