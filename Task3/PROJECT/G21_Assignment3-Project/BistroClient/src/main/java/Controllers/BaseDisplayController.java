package Controllers;

import java.util.List;
import Entities.Reservation;

/**
 * Abstract base controller for displaying reservations.
 * Classes extending this controller must implement the logic to show reservations.
 */
public abstract class BaseDisplayController {

    /**
     * Called when a list of reservations is received.
     * Implementing classes should define how the reservations are displayed.
     *
     * @param list a list of {@link Reservation} objects to be displayed
     */
    public abstract void showReservations(List<Reservation> list);
}
