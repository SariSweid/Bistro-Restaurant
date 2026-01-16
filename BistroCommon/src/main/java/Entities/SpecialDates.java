package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a special date for the restaurant with different opening and closing hours.
 * Extends {@link OpeningHours} to include specific opening and closing times for the date.
 * Stores the date and an optional description.
 */
@SuppressWarnings("serial")
public class SpecialDates extends OpeningHours implements Serializable {

    private LocalDate date;
    private String description;

    /**
     * Constructs a new special date with specified opening and closing times, date, and description.
     *
     * @param openingTime the opening time for the special date
     * @param closingTime the closing time for the special date
     * @param date        the specific date of the special schedule
     * @param description a description of the special date (e.g., holiday, event)
     */
    public SpecialDates(LocalTime openingTime, LocalTime closingTime, LocalDate date, String description) {
        super(openingTime, closingTime);
        this.date = date;
        this.description = description;
    }

    /**
     * Returns the date of the special schedule.
     *
     * @return the special date
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Returns the description of the special date.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets a new date for the special schedule.
     *
     * @param newDate the new date
     */
    public void setDate(LocalDate newDate) {
        this.date = newDate;
    }

    /**
     * Sets a new description for the special date.
     *
     * @param newDescription the new description
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
}
