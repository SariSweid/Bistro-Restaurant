package logicControllers;

import Entities.WeekData;
import enums.ReportType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import DB.DBController;

public class SubscribesReportController {

    private final DBController db = new DBController();

    /**
     * Generates weekly report data for the given type between start and end dates.
     * @param type Report type (SUBSCRIBERS or SCHEDULE)
     * @param startDate first day of the month
     * @param endDate last day of the month
     * @return list of WeekData
     */
    public List<WeekData> generateReportData(LocalDate startDate, LocalDate endDate) {
        List<WeekData> data = new ArrayList<>();

        LocalDate weekStart = startDate;
        int weekNum = 1;

        while (!weekStart.isAfter(endDate)) {
            LocalDate weekEnd = weekStart.plusDays(6);
            if (weekEnd.isAfter(endDate)) weekEnd = endDate;

            int completed = db.getCompletedSubscriberReservationsBetween(weekStart, weekEnd);
            int waitlist = db.getWaitlistSubscriberReservationsBetween(weekStart, weekEnd);

            data.add(new WeekData("Week " + weekNum, completed, waitlist));

            weekStart = weekEnd.plusDays(1);
            weekNum++;
        }

        return data;
    }
}
