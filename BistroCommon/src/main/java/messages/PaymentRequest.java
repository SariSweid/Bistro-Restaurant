package messages;

import java.io.Serializable;

public class PaymentRequest implements Serializable {
	private final int confirmationCode;
	
    public PaymentRequest(int confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

	public int getConfirmationCode() {
		return confirmationCode;
	}
	

}
