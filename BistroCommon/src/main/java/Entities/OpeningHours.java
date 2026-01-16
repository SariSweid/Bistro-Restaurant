package Entities;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Abstract base class representing the opening and closing hours of the restaurant.
 * Used as a superclass for specific types of opening hours, such as WeeklyOpeningHours and SpecialDates.
 */
@SuppressWarnings("serial")
public abstract class OpeningHours implements Serializable {

    /**
     * The opening time of the restaurant.
     */
    private LocalTime openingTime;

    /**
     * The closing time of the restaurant.
     */
    private LocalTime closingTime;

    /**
     * Constructs a new OpeningHours instance with the specified opening and closing times.
     *
     * @param openingTime the time the restaurant opens
     * @param closingTime the time the restaurant closes
     */
    protected OpeningHours(LocalTime openingTime, LocalTime closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    /**
     * Returns the opening time of the restaurant.
     *
     * @return the opening time
     */
    public LocalTime getOpeningTime() {
        return this.openingTime;
    }

    /**
     * Returns the closing time of the restaurant.
     *
     * @return the closing time
     */
    public LocalTime getClosingTime() {
        return this.closingTime;
    }

    /**
     * Sets a new opening time for the restaurant.
     *
     * @param newOpeningTime the new opening time to set
     */
    public void setOpeningTime(LocalTime newOpeningTime) {
        this.openingTime = newOpeningTime;
    }

    /**
     * Sets a new closing time for the restaurant.
     *
     * @param newClosingTime the new closing time to set
     */
    public void setClosingTime(LocalTime newClosingTime) {
        this.closingTime = newClosingTime;
    }
}
