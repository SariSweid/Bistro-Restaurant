package logicControllers;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import DAO.ReservationDAO;
import Entities.TimeData;

/**
 * Controller responsible for generating statistical reports regarding time management.
 * It analyzes gaps between scheduled reservations, actual guest arrivals, and departures
 * to help management understand customer behavior and restaurant efficiency.
 */
public class TimeReportController {

    private final ReservationDAO db = new ReservationDAO();

    /**
     * Fetches raw time-related data for reservations within a specific date range.
     * * @param start The start date of the report period.
     * @param end   The end date of the report period.
     * @return A list of TimeData objects containing reservation, arrival, and departure timestamps.
     */
    public List<TimeData> generateReportData(LocalDate start, LocalDate end) {
        return db.getTimeDataBetween(start, end);
    }

    /**
     * Categorizes and counts late arrivals into specific time buckets.
     * Buckets are defined as: 0-5 minutes, 5-10 minutes, and 10-15 minutes late.
     * This metric helps evaluate the 15-minute grace period policy.
     *
     * @param list A list of TimeData records to analyze.
     * @return A Map where keys are time ranges (String) and values are the frequency of occurrences (Integer).
     */
    public Map<String, Integer> calculateLateArrivals(List<TimeData> list) {
        // LinkedHashMap is used to maintain the insertion order for consistent UI display
        Map<String, Integer> lateArrivals = new LinkedHashMap<>();
        lateArrivals.put("0-5", 0);
        lateArrivals.put("5-10", 0);
        lateArrivals.put("10-15", 0);

        for (TimeData t : list) {
            if (t.getReservationTime() != null && t.getActualArrivalTime() != null) {
                long late = Duration.between(t.getReservationTime(), t.getActualArrivalTime()).toMinutes();
                
                if (late > 0) {
                    if (late <= 5) lateArrivals.put("0-5", lateArrivals.get("0-5") + 1);
                    else if (late <= 10) lateArrivals.put("5-10", lateArrivals.get("5-10") + 1);
                    else if (late <= 15) lateArrivals.put("10-15", lateArrivals.get("10-15") + 1);
                }
            }
        }
        return lateArrivals;
    }

    /**
     * Calculates delays in table turnover by comparing the expected departure time 
     * (defined as Reservation Time + 2 hours) with the actual departure time.
     *
     * @param list A list of TimeData records to analyze.
     * @return A Map of delay categories (e.g., "10-20" minutes) and their respective counts.
     */
    public Map<String, Integer> calculateDelays(List<TimeData> list) {
        Map<String, Integer> delays = new LinkedHashMap<>();
        delays.put("0-10", 0);
        delays.put("10-20", 0);
        delays.put("20-30", 0);
        delays.put("30-40", 0);
        delays.put("40-50", 0);
        delays.put("50-60", 0);

        for (TimeData t : list) {
            if (t.getReservationTime() != null && t.getDepartureTime() != null) {
                // Assuming a standard dining window of 2 hours
                long delay = Duration.between(t.getReservationTime().plusHours(2), t.getDepartureTime()).toMinutes();
                
                if (delay > 0) {
                    if (delay <= 10) delays.put("0-10", delays.get("0-10") + 1);
                    else if (delay <= 20) delays.put("10-20", delays.get("10-20") + 1);
                    else if (delay <= 30) delays.put("20-30", delays.get("20-30") + 1);
                    else if (delay <= 40) delays.put("30-40", delays.get("30-40") + 1);
                    else if (delay <= 50) delays.put("40-50", delays.get("40-50") + 1);
                    else delays.put("50-60", delays.get("50-60") + 1);
                }
            }
        }
        return delays;
    }
}