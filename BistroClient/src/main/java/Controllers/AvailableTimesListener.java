package Controllers;

import java.time.LocalTime;
import java.util.List;

public interface AvailableTimesListener {
    void updateAvailableTimes(List<LocalTime> times);
}
