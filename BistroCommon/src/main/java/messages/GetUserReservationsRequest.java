package messages;

import java.io.Serializable;

/**
 * Represents a request message to retrieve all reservations associated with a specific customer.
 * This is used to fetch the booking history or upcoming reservations for a single user 
 * identified by their unique customer ID.
 */
@SuppressWarnings("serial")
public class GetUserReservationsRequest implements Serializable {

    /**
     * The unique identifier (ID) of the customer whose reservations are being requested.
     */
    private final int customerId;

    /**
     * Constructs a new GetUserReservationsRequest with the specified customer ID.
     *
     * @param customerId the unique ID of the customer
     */
    public GetUserReservationsRequest(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns the customer ID associated with this request.
     *
     * @return the unique identifier of the customer
     */
    public int getCustomerId() {
        return customerId;
    }
}