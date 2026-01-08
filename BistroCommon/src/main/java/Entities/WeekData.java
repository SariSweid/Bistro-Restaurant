package Entities;

import java.io.Serializable;

public class WeekData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String weekName;
    private int subscribers;
    private int reservations;

    public WeekData(String weekName, int subscribers, int reservations) {
        this.weekName = weekName;
        this.subscribers = subscribers;
        this.reservations = reservations;
    }

    public String getWeekName() {
        return weekName;
    }

    public int getSubscribers() {
        return subscribers;
    }

    public int getReservations() {
        return reservations;
    }
}
