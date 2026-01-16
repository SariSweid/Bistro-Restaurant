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

public class RestaurantSettingsController {
	
    private final RestaurantSettings restaurantSettings;
    private final RestaurantSettingsDAO dao;
    private final SpecialDatesDAO SPdao;
	private final ReservationDAO reservationDAO;

    public RestaurantSettingsController() {
        this.restaurantSettings = RestaurantSettings.getInstance();
        this.dao = new RestaurantSettingsDAO();
        this.SPdao = new SpecialDatesDAO();
        this.reservationDAO = new ReservationDAO();
    }

    public RestaurantSettings getRestaurantSettings() {
        return restaurantSettings;
    }

    public int getMaxTables() {
        return this.restaurantSettings.getMaxTables();
    }

    public boolean updateMaxTables(int newMaxTables) {
        this.restaurantSettings.setMaxTables(newMaxTables);
        return this.dao.updateMaxTable(restaurantSettings);
    }

    public WeeklyOpeningHours getOpeningHoursForDay(Day day) {
        return this.restaurantSettings.getOpeningHoursForDay(day);
    }

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
	 * @return all weekly opening hours
	 */
	public List<WeeklyOpeningHours> getAllWeeklyOpeningHours(){
		List<WeeklyOpeningHours> hoursList = new ArrayList<>();
		hoursList = dao.getAllWeeklyOpeningHours();
		System.out.println(hoursList);
		restaurantSettings.setWeeklyOpeningHours(hoursList);
		return hoursList;
	}

    public List<SpecialDates> getAllSpecialDates() {
        List<SpecialDates> specialList = SPdao.getAllSpecialDates();
        restaurantSettings.setSpecialDates(specialList);
        return specialList;
    }
    
    

    public boolean addSpecialDate(AddSpecialDateRequest specialDate) {
    	
        restaurantSettings.addSpecialDate(specialDate.getSpecialDate());
        return SPdao.addSpecialDates(specialDate.getSpecialDate());
    }
    
    public WeeklyOpeningHours getHoursForDay(LocalDate date) {

    	Day day = Day.valueOf(date.getDayOfWeek().name());

    	WeeklyOpeningHours hours = getOpeningHoursForDay(day);

        return hours; 
    }

    

    /**
     * Creates a new weekly opening hours entry for a day.
     * If the day already exists in DB, updates its times instead.
     *
     * @param hours WeeklyOpeningHours object with day, openingTime, closingTime
     * @return true if inserted or updated successfully
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
     * Removes a weekly opening hours entry for a specific day.
     *
     * @param day the day to remove
     * @return true if removed successfully
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
     * Checks if a day exists in memory (restaurantSettings object)
     *
     * @param day Day to check
     * @return true if day exists
     */
    public boolean isDayExisting(Day day) {
        return restaurantSettings.getWeeklyOpeningHours().stream()
                .anyMatch(h -> h.getDay() == day);
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
	 * add special date to the restaurant
	 * @param specialDate
	 * @return true if the date was added to the db
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
	
	
	public void cancelConflictingReservations(WeeklyOpeningHours newHours) {
	    // Get all future reservations for this specific day of the week
	    List<Reservation> reservations = reservationDAO.getReservationsByDay(newHours.getDay());
	    
	    for (Reservation res : reservations) {
	        // Only check active reservations
	        if (res.isReservationActive()) {
	            LocalTime time = res.getReservationTime();
	            
	            // If the reservation is now outside the new hours
	            if (time.isBefore(newHours.getOpeningTime()) || time.isAfter(newHours.getClosingTime())) {
	                System.out.println("SIMULATION: Reservation " + res.getReservationID() + " is now out of bounds. Cancelling.");
	                
	                // Pass 'true' to indicate this was a system cancellation 
	                // and the user needs to be notified.
	                reservationDAO.cancelReservationInDB(res.getReservationID(), true);
	            }
	        }
	    }
	}
	
}
