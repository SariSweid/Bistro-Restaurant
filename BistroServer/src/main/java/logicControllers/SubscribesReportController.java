package logicControllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DAO.ReservationDAO;
import DAO.WaitingListDAO;
import Entities.WeekData;

/**
 * Controller to generate weekly subscribers reports.
 * Uses SubscribesReportDAO to access DB.
 */
public class SubscribesReportController {

    private final ReservationDAO dao;
    private final WaitingListDAO waitDao;

    // Constructor
    public SubscribesReportController() {
        this.dao = new ReservationDAO(); // DAO handles DB operations
        this.waitDao = new WaitingListDAO(); // DAO for waiting 
    }

    /**
     * Generates weekly report data for the given type between start and end dates.
     *
     * @param startDate first day of the month
     * @param endDate   last day of the month
     * @return list of WeekData
     */
    public List<WeekData> generateReportData(LocalDate startDate, LocalDate endDate) {
        List<WeekData> data = new ArrayList<>();

        LocalDate weekStart = startDate;
        int weekNum = 1;

        while (!weekStart.isAfter(endDate)) {
            LocalDate weekEnd = weekStart.plusDays(6);
            if (weekEnd.isAfter(endDate)) weekEnd = endDate;

            int completed = dao.getCompletedSubscriberReservationsBetween(weekStart, weekEnd);
            int waitlist = waitDao.getWaitlistSubscriberCountBetween(weekStart, weekEnd);

            data.add(new WeekData("Week " + weekNum, completed, waitlist));

            weekStart = weekEnd.plusDays(1);
            weekNum++;
        }

        return data;
    }
}
