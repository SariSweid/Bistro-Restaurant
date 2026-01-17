package logicControllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import DAO.ReservationDAO;
import DAO.RestaurantSettingsDAO;
import DAO.SpecialDatesDAO;
import Entities.Reservation;
import Entities.RestaurantSettings;
import Entities.SpecialDates;
import Entities.WeeklyOpeningHours;
import enums.Day;
import messages.AddSpecialDateRequest;

/**
 * Controller class responsible for managing restaurant settings, including opening hours,
 * special dates, and table capacity. It coordinates between the Data Access Objects (DAOs)
 * and the application state.
 */
public class RestaurantSettingsController {
	
    /** The singleton instance of the restaurant settings. */
    private final RestaurantSettings restaurantSettings;
    
    /** Data access object for general restaurant settings. */
    private final RestaurantSettingsDAO dao;
    
    /** Data access object for special holiday or exceptional dates. */
    private final SpecialDatesDAO SPdao;
    
    /** Data access object for handling reservations. */
	private final ReservationDAO reservationDAO;

    /**
     * Initializes a new RestaurantSettingsController and instantiates the necessary DAOs.
     */
    public RestaurantSettingsController() {
        this.restaurantSettings = RestaurantSettings.getInstance();
        this.dao = new RestaurantSettingsDAO();
        this.SPdao = new SpecialDatesDAO();
        this.reservationDAO = new ReservationDAO();
    }

    /**
     * Retrieves the current restaurant settings from the database.
     * Fetches both the weekly opening hours and special dates, ensuring
     * the returned object is up-to-date with the database.
     *
     * @return a RestaurantSettings object populated with data from the DB.
     */
    public RestaurantSettings getRestaurantSettings() {
        RestaurantSettings settings = new RestaurantSettings();
        settings.setWeeklyOpeningHours(dao.getAllWeeklyOpeningHours());
        settings.setSpecialDates(SPdao.getAllSpecialDates());
        return settings;
    }

    /**
     * Gets the maximum number of tables available in the restaurant.
     * @return the total number of tables.
     */
    public int getMaxTables() {
        return this.restaurantSettings.getMaxTables();
    }

    /**
     * Updates the maximum table capacity in the system and the database.
     * @param newMaxTables the new maximum number of tables.
     * @return true if the database update was successful, false otherwise.
     */
    public boolean updateMaxTables(int newMaxTables) {
        this.restaurantSettings.setMaxTables(newMaxTables);
        return this.dao.updateMaxTable(restaurantSettings);
    }

    /**
     * Retrieves the standard opening hours for a specific day of the week.
     * @param day the day of the week (e.g., MONDAY).
     * @return the WeeklyOpeningHours for that day.
     */
    public WeeklyOpeningHours getOpeningHoursForDay(Day day) {
        return this.restaurantSettings.getOpeningHoursForDay(day);
    }

    /**
     * Retrieves the opening hours for a specific calendar date. 
     * Checks if a special date configuration exists first; otherwise, returns the standard weekly hours.
     * @param date the specific date to check.
     * @return the opening and closing times applicable to that date.
     */
    public WeeklyOpeningHours getOpeningHoursForDate(LocalDate date) {
        for (SpecialDates sp : restaurantSettings.getSpecialDates()) {
            if (sp.getDate().equals(date)) {
                return new WeeklyOpeningHours(
                        sp.getOpeningTime(),
                        sp.getClosingTime(),
                        Day.valueOf(date.getDayOfWeek().name())
                );
            }
        }

        Day day = Day.valueOf(date.getDayOfWeek().name());
        return restaurantSettings.getOpeningHoursForDay(day);
    }
    
	/**
	 * Fetches all weekly opening hours from the database and updates the local settings.
	 * @return a list of all WeeklyOpeningHours entries.
	 */
	public List<WeeklyOpeningHours> getAllWeeklyOpeningHours(){
		List<WeeklyOpeningHours> hoursList = new ArrayList<>();
		hoursList = dao.getAllWeeklyOpeningHours();
		System.out.println(hoursList);
		restaurantSettings.setWeeklyOpeningHours(hoursList);
		return hoursList;
	}

    /**
     * Fetches all special dates from the database and updates the local settings.
     * @return a list of all SpecialDates.
     */
    public List<SpecialDates> getAllSpecialDates() {
        List<SpecialDates> specialList = SPdao.getAllSpecialDates();
        restaurantSettings.setSpecialDates(specialList);
        return specialList;
    }

    /**
     * Adds a special date to the system via a request message object.
     * @param specialDate the request object containing the special date details.
     * @return true if added successfully to the database.
     */
    public boolean addSpecialDate(AddSpecialDateRequest specialDate) {
        restaurantSettings.addSpecialDate(specialDate.getSpecialDate());
        return SPdao.addSpecialDates(specialDate.getSpecialDate());
    }
    
    /**
     * Helper method to get the standard weekly opening hours for a specific date.
     * @param date the date used to determine the day of the week.
     * @return the standard WeeklyOpeningHours for that day.
     */
    public WeeklyOpeningHours getHoursForDay(LocalDate date) {
    	Day day = Day.valueOf(date.getDayOfWeek().name());
    	WeeklyOpeningHours hours = getOpeningHoursForDay(day);
        return hours; 
    }

    /**
     * Creates a new weekly opening hours entry or updates an existing one.
     * If hours are updated, it triggers a check to cancel reservations that fall outside the new times.
     * @param hours the WeeklyOpeningHours object to save.
     * @return true if the operation was successful in the database.
     */
    public boolean createOrUpdateWeeklyOpeningHours(WeeklyOpeningHours hours) {
        boolean existsInDB = dao.existsDay(hours.getDay());
        boolean success;

        if (existsInDB) {
            boolean openingUpdated = dao.updateOpeningHours(hours);
            boolean closingUpdated = dao.updateClosingHours(hours);
            restaurantSettings.getWeeklyOpeningHours().removeIf(h -> h.getDay() == hours.getDay());
            restaurantSettings.addWeeklyOpeningHour(hours);
            success = openingUpdated && closingUpdated;
        } else {
            restaurantSettings.addWeeklyOpeningHour(hours);
            success = dao.insertWeeklyOpeningHours(hours);
        }
        
        if (success) {
            System.out.println("Hours updated successfully for " + hours.getDay() + " Checking for conflicts...");
            cancelConflictingReservations(hours);
        }

        return success;
    }
    
    /**
     * Removes a weekly opening hours entry for a specific day and cancels all reservations on that day.
     * @param day the day of the week to remove.
     * @return true if the day was successfully deleted from the database.
     */
    public boolean removeWeeklyOpeningHours(Day day) {
        boolean removedFromDB = dao.deleteWeeklyOpeningHours(day);
        if (removedFromDB) {
            restaurantSettings.getWeeklyOpeningHours().removeIf(h -> h.getDay() == day);

            List<Reservation> reservations = reservationDAO.getReservationsByDay(day);
            for (Reservation r : reservations) {
                reservationDAO.cancelReservationInDB(r.getReservationID(), true);
            }

            return true;
        }
        return false;
    }

    /**
     * Checks if a specific day of the week has opening hours defined in the local settings.
     * @param day the day to check.
     * @return true if the day exists in settings, false otherwise.
     */
    public boolean isDayExisting(Day day) {
        return restaurantSettings.getWeeklyOpeningHours().stream()
                .anyMatch(h -> h.getDay() == day);
    }

	/**
	 * Adds a special date to the restaurant settings and database.
	 * @param specialDate the SpecialDates object to add.
	 * @return true if added to the database successfully.
	 */
	public boolean addSpecialDate(SpecialDates specialDate) {
		this.restaurantSettings.addSpecialDate(specialDate);
		return this.SPdao.addSpecialDates(specialDate);
	}
	
	/**
	 * Deletes a special date and cancels all reservations scheduled for that date.
	 * @param date the specific date to remove.
	 * @return true if the date was successfully deleted from the database.
	 */
	public boolean deleteSpecialDate(LocalDate date) {
	    boolean flag = this.SPdao.deleteSpecialDate(date);
	    if (flag) {
	        this.restaurantSettings.getSpecialDates().removeIf(s -> s.getDate().equals(date));
	        
	        List<Reservation> reservations = this.reservationDAO.getReservationsByDate(date);
	        for (Reservation r : reservations) {
	            reservationDAO.cancelReservationInDB(r.getReservationID(), true);
	        }
	    }
	    return flag;
	}

	/**
	 * Updates an existing special date entry with new details.
	 * @param oldDate the original date to be replaced/updated.
	 * @param specialDate the new SpecialDates object with updated information.
	 * @return true if the database update was successful.
	 */
	public boolean updateSpecialDate(LocalDate oldDate, SpecialDates specialDate) {
		boolean flag = this.SPdao.updateSpecialDates(oldDate, specialDate);
		if(flag) {
			this.restaurantSettings.getSpecialDates().removeIf(s -> s.getDate().equals(oldDate));
			this.restaurantSettings.addSpecialDate(specialDate);
		}
		
		return flag;
	}
	
	/**
	 * Identifies and cancels any future reservations that conflict with newly updated opening hours.
	 * Reservations falling outside the new opening or closing times will be cancelled.
	 * @param newHours the new WeeklyOpeningHours used for the conflict check.
	 */
	public void cancelConflictingReservations(WeeklyOpeningHours newHours) {
	    List<Reservation> reservations = reservationDAO.getReservationsByDay(newHours.getDay());
	    
	    for (Reservation res : reservations) {
	        if (res.isReservationActive()) {
	            LocalTime time = res.getReservationTime();
	            
	            if (time.isBefore(newHours.getOpeningTime()) || time.isAfter(newHours.getClosingTime())) {
	                System.out.println("SIMULATION: Reservation " + res.getReservationID() + " is now out of bounds. Cancelling.");
	                reservationDAO.cancelReservationInDB(res.getReservationID(), true);
	            }
	        }
	    }
	}
}