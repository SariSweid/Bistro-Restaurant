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
            if (weekEnd.isAfter(monthEnd)) {
                weekEnd = monthEnd;
            }

            int value1 = 0;
            int value2 = 0;

            if (type == ReportType.SCHEDULE) {
                value1 = db.getReservationsCountBetween(weekStart, weekEnd);
            }
            else if (type == ReportType.SUBSCRIBERS) {
                value1 = db.getCompletedSubscriberReservationsBetween(weekStart, weekEnd);
                value2 = db.getWaitlistSubscriberReservationsBetween(weekStart, weekEnd);
            }

            data.add(new WeekData("Week " + weekNum,value1,value2));

            weekStart = weekEnd.plusDays(1);
            weekNum++;
        }

        return data;
    }
}
