package messages;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class GetAvailableTimesRequest implements Serializable {
    private final LocalDate date;
    private final int guests;
    private boolean forWaitingList;

    public GetAvailableTimesRequest(LocalDate date, int guests,boolean forWaitingList) {
        this.date = date;
        this.guests = guests;
        this.forWaitingList = forWaitingList;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getGuests() {
        return guests;
    }
    public boolean isForWaitingList() {
    	return forWaitingList; 
    	}
}
