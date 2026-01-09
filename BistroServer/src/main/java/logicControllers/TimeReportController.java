package logicControllers;

import Entities.TimeData;
import DB.DBController;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class TimeReportController {

    private final DBController db = new DBController();

    /**
     * Generates the list of TimeData for the previous month from DB.
     * @return list of TimeData
     */
    public List<TimeData> generateReportData() {
        YearMonth month = YearMonth.now().minusMonths(1);
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<TimeData> list = db.getTimeDataBetween(start, end);

        for (TimeData t : list) {
            System.out.println(
                "Reservation: " + t.getReservationTime() +
                " | Actual Arrival: " + t.getActualArrivalTime() +
                " | Departure: " + t.getDepartureTime()
            );
        }

        return list;
    }

    /**
     * Calculates the count of late arrivals by category.
     * Categories: 0-5, 5-10, 10-15
     * @param list list of TimeData
     * @return map of category -> count
     */
    public Map<String, Integer> calculateLateArrivals(List<TimeData> list) {
        Map<String, Integer> lateArrivals = new LinkedHashMap<>();
        lateArrivals.put("0-5", 0);
        lateArrivals.put("5-10", 0);
        lateArrivals.put("10-15", 0);

        for (TimeData t : list) {
            if (t.getReservationTime() != null && t.getActualArrivalTime() != null) {
                long late = Duration.between(t.getReservationTime(), t.getActualArrivalTime()).toMinutes();
                if (late > 0) {
                    if (late <= 5) lateArrivals.put("0-5", lateArrivals.get("0-5")+1);
                    else if (late <= 10) lateArrivals.put("5-10", lateArrivals.get("5-10")+1);
                    else if (late <= 15) lateArrivals.put("10-15", lateArrivals.get("10-15")+1);
                }
            }
        }
        return lateArrivals;
    }

    /**
     * Calculates the count of delays by category.
     * Categories: 0-10, 10-20, 20-30, 30-40, 40-50, 50-60
     * @param list list of TimeData
     * @return map of category -> count
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
                long delay = Duration.between(t.getReservationTime().plusHours(2), t.getDepartureTime()).toMinutes();
                if (delay > 0) {
                    if (delay <= 10) delays.put("0-10", delays.get("0-10")+1);
                    else if (delay <= 20) delays.put("10-20", delays.get("10-20")+1);
                    else if (delay <= 30) delays.put("20-30", delays.get("20-30")+1);
                    else if (delay <= 40) delays.put("30-40", delays.get("30-40")+1);
                    else if (delay <= 50) delays.put("40-50", delays.get("40-50")+1);
                    else delays.put("50-60", delays.get("50-60")+1);
                }
            }
        }
        return delays;
    }

}
