package logicControllers;

import Entities.TimeData;
import DB.DBController;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class TimeReportController {

    private final DBController db = new DBController();

    public List<TimeData> generateReportData() {
        YearMonth month = YearMonth.now().minusMonths(1);
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<TimeData> list = db.getTimeDataBetween(start, end);

        for (TimeData t : list) {
            System.out.println(
                "Arrival: " + t.getArrivalTime() +
                " | Departure: " + t.getDepartureTime()
            );
        }

        return list;
    }
}
