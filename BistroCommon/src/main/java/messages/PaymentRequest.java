package messages;

import java.io.Serializable;
import java.time.LocalTime;

@SuppressWarnings("serial")
public class PaymentRequest implements Serializable {
	private final int confirmationCode;
	private LocalTime depatureTime;
	
    public PaymentRequest(int confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

	public int getConfirmationCode() {
		return confirmationCode;
	}

	
	// new getter/setter
    public LocalTime getDepartureTime() { return depatureTime; }
    

	public void setDepartureTime(LocalTime departureTime) {
		this.depatureTime = departureTime;
		
	}
	

}
