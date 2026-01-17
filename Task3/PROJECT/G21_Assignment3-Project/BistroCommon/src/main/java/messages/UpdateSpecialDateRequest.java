package messages;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a request message to update the details of an existing special date in the system.
 * This request contains the original date to identify the record and the new details to be applied.
 */
@SuppressWarnings("serial")
public class UpdateSpecialDateRequest implements Serializable {

    /**
     * The original date of the record that needs to be updated.
     */
    private LocalDate oldDate;

    /**
     * The new description for the special date.
     */
    private String description;

    /**
     * The new date to be set for this special date record.
     */
    private LocalDate date;

    /**
     * The new opening time for the restaurant on this special date.
     */
    private LocalTime openingTime;

    /**
     * The new closing time for the restaurant on this special date.
     */
    private LocalTime closingTime;

    /**
     * Constructs a new UpdateSpecialDateRequest with the specified details.
     *
     * @param oldDate     the original date identifying the record to update
     * @param description the new description for the special date
     * @param date        the new date to be assigned
     * @param openingTime the new opening time
     * @param closingTime the new closing time
     */
    public UpdateSpecialDateRequest(LocalDate oldDate, String description, LocalDate date, LocalTime openingTime, LocalTime closingTime) {
        this.oldDate = oldDate;
        this.description = description;
        this.date = date;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    /**
     * Returns the original date associated with the record being updated.
     *
     * @return the old LocalDate
     */
    public LocalDate getOldDate() {
        return oldDate;
    }

    /**
     * Returns the new closing time associated with this request.
     *
     * @return the new closing LocalTime
     */
    public LocalTime getClosingTime() {
        return closingTime;
    }

    /**
     * Returns the new date associated with this request.
     *
     * @return the new LocalDate
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the new opening time associated with this request.
     *
     * @return the new opening LocalTime
     */
    public LocalTime getOpeningTime() {
        return openingTime;
    }

    /**
     * Returns the new description associated with this request.
     *
     * @return the new description string
     */
    public String getDescription() {
        return description;
    }
}