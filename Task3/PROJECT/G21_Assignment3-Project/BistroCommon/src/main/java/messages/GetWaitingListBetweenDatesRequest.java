package messages;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a request message to retrieve the waiting list entries within a specific date range.
 * This is used to fetch all waiting list records starting from a start date up to an end date.
 */
@SuppressWarnings("serial")
public class GetWaitingListBetweenDatesRequest implements Serializable {

    /**
     * The start date of the range for the waiting list retrieval.
     */
    private LocalDate startDate;

    /**
     * The end date of the range for the waiting list retrieval.
     */
    private LocalDate endDate;

    /**
     * Constructs a new GetWaitingListBetweenDatesRequest with the specified date range.
     *
     * @param startDate the beginning of the date range (inclusive)
     * @param endDate   the end of the date range (inclusive)
     */
    public GetWaitingListBetweenDatesRequest(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the start date of the requested range.
     *
     * @return the LocalDate representing the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the end date of the requested range.
     *
     * @return the LocalDate representing the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }
}