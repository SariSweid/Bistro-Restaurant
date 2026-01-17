package Controllers;

import Entities.ReportSession;
import enums.ReportType;
import handlers.ClientHandler;
import javafx.fxml.FXML;

/**
 * Controller for generating and displaying the times (schedule) report.
 * Extends ReportController and requests schedule report data
 * for the selected month and year from the server.
 */
public class TimesReportController extends ReportController {

    /**
     * Returns the type of report this controller handles.
     * @return ReportType.SCHEDULE
     */
    @Override
    protected ReportType getReportType() {
        return ReportType.SCHEDULE;
    }

    /**
     * Initializes the controller.
     * Calls the base initialization, retrieves the month and year from ReportSession,
     * and requests the schedule report from the server via ClientHandler.
     */
    @FXML
    public void initialize() {
        super.initialize();

        int month = ReportSession.getMonth();
        int year = ReportSession.getYear();

        ClientHandler.getClient().requestReport(ReportType.SCHEDULE, year, month, this);
    }
}
