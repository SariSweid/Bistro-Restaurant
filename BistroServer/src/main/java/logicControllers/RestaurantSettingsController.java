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
import messages.AddSpecialDateRequest;

public class RestaurantSettingsController {
    private final RestaurantSettings restaurantSettings;
    private final RestaurantSettingsDAO dao;
    private final SpecialDatesDAO SPdao;

    public RestaurantSettingsController() {
        this.restaurantSettings = RestaurantSettings.getInstance();
        this.dao = new RestaurantSettingsDAO();
        this.SPdao = new SpecialDatesDAO();
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

    public boolean updateSpecialDate(LocalDate oldDate, SpecialDates specialDate) {
        boolean flag = SPdao.updateSpecialDates(oldDate, specialDate);
        if (flag) {
            restaurantSettings.getSpecialDates().removeIf(s -> s.getDate().equals(oldDate));
            restaurantSettings.addSpecialDate(specialDate);
        }
        return flag;
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

        return success;
    }
    
    /**
     * Removes a weekly opening hours entry for a specific day.
     *
     * @param day the day to remove
     * @return true if removed successfully
     */
    public boolean removeWeeklyOpeningHours(Day day) {
    	System.out.println("asdasdasdasdasd");
        boolean removedFromDB = dao.deleteWeeklyOpeningHours(day);
        if (removedFromDB) {
            restaurantSettings.getWeeklyOpeningHours().removeIf(h -> h.getDay() == day);
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
}
