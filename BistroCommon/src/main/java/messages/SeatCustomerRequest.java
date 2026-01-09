package messages;

import java.io.Serializable;
import java.time.LocalTime;

public class SeatCustomerRequest implements Serializable {

    private int userId;
    private int confirmationCode;
    private LocalTime actualArrivalTime;

    public SeatCustomerRequest(int userId, int confirmationCode) {
        this.userId = userId;
        this.confirmationCode = confirmationCode;
    }

    public int getUserId() {
        return userId;
    }

    public int getConfirmationCode() {
        return confirmationCode;
    }

	// new getter/setter
    public LocalTime getActualArrivalTime() { return actualArrivalTime; }
    
    public void setActualArrivalTime(LocalTime actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }
}
