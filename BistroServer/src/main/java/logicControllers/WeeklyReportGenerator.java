package logicControllers;

import Entities.WeekData;
import enums.ReportType;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import DB.DBController;

public class WeeklyReportGenerator {
    private final DBController db = new DBController();

    public List<WeekData> generateReportData(ReportType type) {
        List<WeekData> data = new ArrayList<>();

        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate monthStart = lastMonth.atDay(1);
        LocalDate monthEnd = lastMonth.atEndOfMonth();

        LocalDate weekStart = monthStart;
        int weekNum = 1;

        while (!weekStart.isAfter(monthEnd)) {
            LocalDate weekEnd = weekStart.plusDays(6);
            if (weekEnd.isAfter(monthEnd)) weekEnd = monthEnd;

            int reservationsCount = 0;
            int subscribersCount = 0;

            if (type == ReportType.SCHEDULE) {
                reservationsCount = db.getReservationsCountBetween(weekStart, weekEnd);
            } else if (type == ReportType.SUBSCRIBERS) {
                subscribersCount = db.getSubscribersCountBetween(weekStart, weekEnd);
            }

            data.add(new WeekData("Week " + weekNum, subscribersCount, reservationsCount));

            weekStart = weekEnd.plusDays(1);
            weekNum++;
        }

        return data;
    }
}
