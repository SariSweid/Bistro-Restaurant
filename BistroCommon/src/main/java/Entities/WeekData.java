package Entities;

import java.io.Serializable;

/**
 * Represents weekly data for the restaurant.
 * Stores the week name, number of completed reservations, and number of waitlist entries.
 */
public class WeekData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String weekName;
    private int completed;
    private int waitlist;

    /**
     * Constructs a new WeekData object with the specified values.
     * 
     * @param weekName the name of the week
     * @param completed the number of completed reservations in this week
     * @param waitlist the number of waitlist entries in this week
     */
    public WeekData(String weekName, int completed, int waitlist) {
        this.weekName = weekName;
        this.completed = completed;
        this.waitlist = waitlist;
    }

    /**
     * Returns the name of the week.
     * 
     * @return the week name
     */
    public String getWeekName() {
        return weekName;
    }

    /**
     * Returns the number of completed reservations for this week.
     * 
     * @return the number of completed reservations
     */
    public int getCompleted() {
        return completed;
    }

    /**
     * Returns the number of waitlist entries for this week.
     * 
     * @return the number of waitlist entries
     */
    public int getWaitlist() {
        return waitlist;
    }
}
