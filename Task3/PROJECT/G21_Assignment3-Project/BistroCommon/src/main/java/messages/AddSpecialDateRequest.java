package messages;

import java.io.Serializable;
import Entities.SpecialDates;

/**
 * Represents a request message to add a new special date to the restaurant system.
 * Contains the SpecialDates object to be added.
 */
@SuppressWarnings("serial")
public class AddSpecialDateRequest implements Serializable {

    /**
     * The special date to add to the restaurant system.
     */
    private SpecialDates specialDate;

    /**
     * Constructs a new AddSpecialDateRequest with the specified special date.
     *
     * @param specialDate the SpecialDates object to add
     */
    public AddSpecialDateRequest(SpecialDates specialDate) {
        this.specialDate = specialDate;
    }

    /**
     * Returns the special date associated with this request.
     *
     * @return the SpecialDates object
     */
    public SpecialDates getSpecialDate() {
        return specialDate;
    }
}
