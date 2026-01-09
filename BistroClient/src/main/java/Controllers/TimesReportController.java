package Controllers;

import Entities.ReportSession;
import enums.ReportType;
import handlers.ClientHandler;
import javafx.fxml.FXML;

public class TimesReportController extends ReportController {

    @Override
    protected ReportType getReportType() {
        return ReportType.SCHEDULE;
    }

    @FXML
    public void initialize() {
        super.initialize();

        
        int month = ReportSession.getMonth();
        int year = ReportSession.getYear();

        ClientHandler.getClient().requestReport(ReportType.SCHEDULE, year, month, this);
    }
}
