package logicControllers;

import java.time.LocalDate;
import java.util.List;

import DB.DBController;
import Entities.RestaurantSettings;
import Entities.SpecialDates;
import Entities.WeeklyOpeningHours;
import enums.Day;

/**
 * RestaurantSettingsController manages the restaurant settings
 */
public class RestaurantSettingsController {
	private final RestaurantSettings restaurantSettings;
	private final DBController dbController;
	
	/**
	 * constructor - creates new RestaurantSettingsController
	 */
	public RestaurantSettingsController() {
		this.restaurantSettings = RestaurantSettings.getInstance();
		this.dbController = new DBController();
	    this.restaurantSettings.setWeeklyOpeningHours(dbController.getAllWeeklyOpeningHours());

	        this.restaurantSettings.setSpecialDates(dbController.getAllSpecialDates());
	}
	
	/**
	 * 
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
		return this.dbController.updateMaxTable(restaurantSettings);
	}
	
	/**
	 * gets the opening and closing hours of a given day
	 * @param day
	 * @return true if opening and closing hours of a given day updated in db
	 */
	public WeeklyOpeningHours getOpeningHoursForDay(Day day) {
		return this.restaurantSettings.getOpeningHoursForDay(day);
	}
	
	/**
	 * update opening and closing hours of a given weekday
	 * @param hours
	 * @return true if the opening and closing hours were updated in the db
	 */
	public boolean updateWeeklyOpeningHours(WeeklyOpeningHours hours) {
		//finds the old opening and closing hours by given day in hours.getDay() if its in the list
		this.restaurantSettings.getWeeklyOpeningHours().removeIf(h -> h.getDay() == hours.getDay());
		//add the new opening and closing hours to the list
		this.restaurantSettings.addWeeklyOpeningHour(hours);
		
		//update opening and closing hours in db
		boolean openingUpdate = this.dbController.updateOpeningHours(restaurantSettings, hours.getDay());
		boolean closingUpdate = this.dbController.updateClosingHours(restaurantSettings, hours.getDay());
		
		return openingUpdate && closingUpdate;
	}
	
	/**
	 * 
	 * @return all weekly opening hours
	 */
	public List<WeeklyOpeningHours> getAllWeeklyOpeningHours(){
		System.out.println("Get asdasd");
		return this.restaurantSettings.getWeeklyOpeningHours();
	}
	
	/**
	 * 
	 * @return all special dates
	 */
	public List<SpecialDates> getAllSpecialDates(){
		return this.restaurantSettings.getSpecialDates();
	}
	
	/**
	 * add special date to the restaurant
	 * @param specialDate
	 * @return true if the date was added to the db
	 */
	public boolean addSpecialDate(SpecialDates specialDate) {
		this.restaurantSettings.addSpecialDate(specialDate);
		return this.dbController.addSpecialDates(specialDate);
	}
	
	/**
	 * update a special date
	 * @param specialDate
	 * @return true if the special date was updated in the db
	 */
	public boolean updateSpecialDate(LocalDate oldDate, SpecialDates specialDate) {
		boolean flag = this.dbController.updateSpecialDates(oldDate, specialDate);
		if(flag) {
			this.restaurantSettings.getSpecialDates().removeIf(s -> s.getDate().equals(oldDate));
			this.restaurantSettings.addSpecialDate(specialDate);
		}
		
		return flag;
		
	}
}
