package messages;

import java.io.Serializable;

/**
 * Represents a request message to retrieve all reservations from the restaurant system.
 * This request is used to fetch the complete list of bookings without any specific filters.
 */
@SuppressWarnings("serial")
public class GetAllReservationsRequest implements Serializable {

    /**
     * Constructs a new GetAllReservationsRequest.
     */
    public GetAllReservationsRequest() {
        // Default constructor
    }
}