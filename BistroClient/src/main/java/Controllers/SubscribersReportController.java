package Controllers;

import Entities.ReportSession;
import enums.ReportType;
import handlers.ClientHandler;
import javafx.fxml.FXML;

/**
 * Controller for generating and displaying subscriber reports in the UI.
 * Extends ReportController and requests a subscriber report from the server
 * based on the current report session's month and year.
 */
public class SubscribersReportController extends ReportController {

    /**
     * Returns the type of report this controller handles.
     *
     * @return ReportType.SUBSCRIBERS
     */
    @Override
    protected ReportType getReportType() {
        return ReportType.SUBSCRIBERS;
    }

    /**
     * Initializes the controller.
     * Calls the superclass initialize method and requests the subscriber report
     * from the server for the current month and year.
     */
    @FXML
    public void initialize() {
        super.initialize();

        int month = ReportSession.getMonth();
        int year = ReportSession.getYear();

        ClientHandler.getClient().requestReport(ReportType.SUBSCRIBERS, year, month, this);
    }
}
