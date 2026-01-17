package messages;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Represents a request message to seat a customer at a table.
 * This request is used to finalize the arrival of a customer, verifying them via 
 * a confirmation code and recording their actual arrival time.
 */
@SuppressWarnings("serial")
public class SeatCustomerRequest implements Serializable {

    /**
     * The unique identifier (ID) of the user/customer to be seated.
     */
    private int userId;

    /**
     * The unique confirmation code associated with the customer's reservation.
     */
    private int confirmationCode;

    /**
     * The actual time the customer arrived at the restaurant.
     */
    private LocalTime actualArrivalTime;

    /**
     * Constructs a new SeatCustomerRequest with the specified user ID and confirmation code.
     *
     * @param userId           the unique ID of the customer
     * @param confirmationCode the booking confirmation code
     */
    public SeatCustomerRequest(int userId, int confirmationCode) {
        this.userId = userId;
        this.confirmationCode = confirmationCode;
    }

    /**
     * Returns the user ID associated with this request.
     *
     * @return the unique identifier of the user
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Returns the confirmation code associated with this request.
     *
     * @return the reservation confirmation code
     */
    public int getConfirmationCode() {
        return confirmationCode;
    }

    /**
     * Returns the actual arrival time of the customer.
     *
     * @return the LocalTime of arrival
     */
    public LocalTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    /**
     * Sets the actual arrival time of the customer.
     *
     * @param actualArrivalTime the LocalTime to be recorded as the arrival time
     */
    public void setActualArrivalTime(LocalTime actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }
}