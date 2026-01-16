package messages;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a request message to delete an existing special date from the restaurant system.
 * Contains the specific date (LocalDate) to be removed.
 */
@SuppressWarnings("serial")
public class DeleteSpecialDateRequest implements Serializable {

    /**
     * The specific date to be deleted from the restaurant system.
     */
    private LocalDate date;

    /**
     * Constructs a new DeleteSpecialDateRequest with the specified date.
     *
     * @param date the LocalDate to remove
     */
    public DeleteSpecialDateRequest(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the date associated with this request.
     *
     * @return the LocalDate object
     */
    public LocalDate getDate() {
        return date;
    }
}