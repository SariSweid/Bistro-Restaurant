package Entities;

import java.io.Serializable;

public class WeekData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String weekName;
    private int completed;   
    private int waitlist;    

    public WeekData(String weekName, int completed, int waitlist) {
        this.weekName = weekName;
        this.completed = completed;
        this.waitlist = waitlist;
    }

    public String getWeekName() {
        return weekName;
    }

    public int getCompleted() {
        return completed;
    }

    public int getWaitlist() {
        return waitlist;
    }
}
