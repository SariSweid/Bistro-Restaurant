package messages;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Represents a request message to process a payment and finalize a reservation.
 * This request uses the reservation's confirmation code to identify the transaction
 * and records the customer's departure time from the restaurant.
 */
@SuppressWarnings("serial")
public class PaymentRequest implements Serializable {

    /**
     * The unique confirmation code associated with the reservation being paid for.
     */
    private final int confirmationCode;

    /**
     * The time the customer departed from the restaurant.
     */
    private LocalTime depatureTime;

    /**
     * Constructs a new PaymentRequest with the specified confirmation code.
     *
     * @param confirmationCode the unique booking confirmation code
     */
    public PaymentRequest(int confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    /**
     * Returns the confirmation code associated with this payment request.
     *
     * @return the reservation confirmation code
     */
    public int getConfirmationCode() {
        return confirmationCode;
    }

    /**
     * Returns the recorded departure time of the customer.
     *
     * @return the LocalTime of departure
     */
    public LocalTime getDepartureTime() {
        return depatureTime;
    }

    /**
     * Sets the departure time for the customer.
     *
     * @param departureTime the LocalTime to be recorded as the departure time
     */
    public void setDepartureTime(LocalTime departureTime) {
        this.depatureTime = departureTime;
    }
}