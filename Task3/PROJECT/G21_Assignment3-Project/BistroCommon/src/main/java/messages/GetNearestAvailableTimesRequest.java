package messages;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a request message to find the nearest available reservation times.
 * This request provides a specific date and the number of guests to check for availability
 * within the restaurant's booking system.
 */
@SuppressWarnings("serial")
public class GetNearestAvailableTimesRequest implements Serializable {

    /**
     * The desired date for the reservation.
     */
    private final LocalDate date;

    /**
     * The number of guests for the reservation.
     */
    private final int guests;

    /**
     * Constructs a new GetNearestAvailableTimesRequest with the specified date and guest count.
     *
     * @param date   the LocalDate to check for availability
     * @param guests the number of people in the reservation party
     */
    public GetNearestAvailableTimesRequest(LocalDate date, int guests) {
        this.date = date;
        this.guests = guests;
    }

    /**
     * Returns the requested reservation date.
     *
     * @return the LocalDate object
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the number of guests associated with this request.
     *
     * @return the guest count
     */
    public int getGuests() {
        return guests;
    }
}