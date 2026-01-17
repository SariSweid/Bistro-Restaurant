package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import enums.Day;

/**
 * Represents the configuration and settings of the restaurant, including
 * maximum tables, weekly opening hours, special dates, and reservation duration.
 * This class is implemented as a singleton to ensure only one instance exists in the system.
 */
@SuppressWarnings("serial")
public class RestaurantSettings implements Serializable {

    /**
     * The single instance of RestaurantSettings.
     */
    private static RestaurantSettings instance;

    /**
     * Maximum number of tables in the restaurant.
     */
    private int maxTables;

    /**
     * List of regular weekly opening hours.
     */
    private List<WeeklyOpeningHours> weeklyOpeningHours;

    /**
     * List of special dates with custom opening and closing hours.
     */
    private List<SpecialDates> specialDates;

    /**
     * Duration of a reservation in hours (default is 2 hours).
     */
    private int reservationDurationHours = 2;

    /**
     * Private constructor to prevent direct instantiation.
     * Initializes the weekly opening hours and special dates lists.
     */
    public RestaurantSettings() {
        this.weeklyOpeningHours = new ArrayList<>();
        this.specialDates = new ArrayList<>();
    }

    /**
     * Returns the single instance of RestaurantSettings.
     * Creates a new instance if it has not been initialized yet.
     *
     * @return the singleton instance of RestaurantSettings
     */
    public static RestaurantSettings getInstance() {
        if (instance == null) {
            instance = new RestaurantSettings();
        }
        return instance;
    }

    /**
     * Returns the maximum number of tables.
     *
     * @return the maximum number of tables
     */
    public int getMaxTables() {
        return this.maxTables;
    }

    /**
     * Sets the maximum number of tables.
     *
     * @param newMaxTables the maximum tables to set
     */
    public void setMaxTables(int newMaxTables) {
        this.maxTables = newMaxTables;
    }

    /**
     * Returns the list of weekly opening hours.
     *
     * @return list of WeeklyOpeningHours
     */
    public List<WeeklyOpeningHours> getWeeklyOpeningHours() {
        return this.weeklyOpeningHours;
    }

    /**
     * Sets the list of weekly opening hours.
     *
     * @param weeklyOpeningHours the list of WeeklyOpeningHours to set
     */
    public void setWeeklyOpeningHours(List<WeeklyOpeningHours> weeklyOpeningHours) {
        this.weeklyOpeningHours = weeklyOpeningHours;
    }

    /**
     * Returns the list of special dates.
     *
     * @return list of SpecialDates
     */
    public List<SpecialDates> getSpecialDates() {
        return this.specialDates;
    }

    /**
     * Sets the list of special dates.
     *
     * @param specialDates the list of SpecialDates to set
     */
    public void setSpecialDates(List<SpecialDates> specialDates) {
        this.specialDates = specialDates;
    }

    /**
     * Returns the reservation duration in hours.
     *
     * @return reservation duration in hours
     */
    public int getReservationDurationHours() {
        return reservationDurationHours;
    }

    /**
     * Sets the reservation duration in hours.
     *
     * @param hours the number of hours for each reservation
     */
    public void setReservationDurationHours(int hours) {
        this.reservationDurationHours = hours;
    }

    /**
     * Returns the weekly opening hours for a given day.
     *
     * @param day the day of the week
     * @return the WeeklyOpeningHours for that day, or null if not found
     */
    public WeeklyOpeningHours getOpeningHoursForDay(Day day) {
        return this.weeklyOpeningHours.stream()
                .filter(e -> e.getDay() == day)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the opening hours for a specific date.
     * Special dates override weekly hours if present.
     *
     * @param date the date to check
     * @return the WeeklyOpeningHours for the date, or null if the restaurant is closed
     */
    public WeeklyOpeningHours getOpeningHoursForDate(java.time.LocalDate date) {

        // Check for special dates override
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

    /**
     * Adds a weekly opening hours entry to the list.
     *
     * @param hours the WeeklyOpeningHours to add
     */
    public void addWeeklyOpeningHour(WeeklyOpeningHours hours) {
        this.weeklyOpeningHours.add(hours);
    }

    /**
     * Adds a special date entry to the list.
     *
     * @param specialDate the SpecialDates entry to add
     */
    public void addSpecialDate(SpecialDates specialDate) {
        this.specialDates.add(specialDate);
    }
}
