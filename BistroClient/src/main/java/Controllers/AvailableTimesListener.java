package Controllers;

import java.time.LocalTime;
import java.util.List;

/**
 * Listener interface for receiving updates about available times.
 * Implementing classes will be notified whenever the list of available times changes.
 */
public interface AvailableTimesListener {

    /**
     * Called when the available times are updated.
     *
     * @param times a list of available times represented as {@link LocalTime} objects
     */
    void updateAvailableTimes(List<LocalTime> times);
}
