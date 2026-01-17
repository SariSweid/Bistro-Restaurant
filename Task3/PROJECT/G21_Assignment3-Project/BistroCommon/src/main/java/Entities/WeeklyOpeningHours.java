package Entities;

import java.io.Serializable;
import java.time.LocalTime;
import enums.Day;

/**
 * Represents the opening and closing hours for a specific weekday.
 * Extends the OpeningHours class by adding the specific day of the week.
 */
@SuppressWarnings("serial")
public class WeeklyOpeningHours extends OpeningHours implements Serializable {

    private Day day;

    /**
     * Constructs a new WeeklyOpeningHours object with the specified opening and closing times and day.
     * 
     * @param openingTime the opening time for the day
     * @param closingTime the closing time for the day
     * @param day the day of the week
     */
    public WeeklyOpeningHours(LocalTime openingTime, LocalTime closingTime, Day day) {
        super(openingTime, closingTime);
        this.day = day;
    }

    /**
     * Returns the day of the week for these opening hours.
     * 
     * @return the day of the week
     */
    public Day getDay() {
        return this.day;
    }

    /**
     * Sets the day of the week for these opening hours.
     * 
     * @param newDay the day of the week to set
     */
    public void setDay(Day newDay) {
        this.day = newDay;
    }
}
