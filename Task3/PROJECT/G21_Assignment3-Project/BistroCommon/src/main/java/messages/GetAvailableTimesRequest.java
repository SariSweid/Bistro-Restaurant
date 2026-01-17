package messages;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a request message to retrieve available time slots for a specific date and party size.
 * This request can also be used to check availability specifically for a waiting list.
 */
@SuppressWarnings("serial")
public class GetAvailableTimesRequest implements Serializable {

    /**
     * The specific date to check for available time slots.
     */
    private final LocalDate date;

    /**
     * The number of guests for the reservation.
     */
    private final int guests;

    /**
     * Indicates whether the availability check is for the waiting list.
     */
    private boolean forWaitingList;

    /**
     * Constructs a new GetAvailableTimesRequest with the specified date, guest count, and waiting list status.
     *
     * @param date           the LocalDate to check
     * @param guests         the number of people in the reservation party
     * @param forWaitingList true if checking availability for the waiting list, false otherwise
     */
    public GetAvailableTimesRequest(LocalDate date, int guests, boolean forWaitingList) {
        this.date = date;
        this.guests = guests;
        this.forWaitingList = forWaitingList;
    }

    /**
     * Returns the requested date for availability.
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

    /**
     * Checks if this request is specifically for the waiting list.
     *
     * @return true if intended for the waiting list, false otherwise
     */
    public boolean isForWaitingList() {
        return forWaitingList;
    }
}