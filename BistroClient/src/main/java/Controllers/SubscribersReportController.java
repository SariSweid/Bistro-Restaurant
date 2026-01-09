package Controllers;

import Entities.ReportSession;
import enums.ReportType;
import handlers.ClientHandler;
import javafx.fxml.FXML;

public class SubscribersReportController extends ReportController {

    @Override
    protected ReportType getReportType() {
        return ReportType.SUBSCRIBERS;
    }

    @FXML
    public void initialize() {
        super.initialize();

        int month = ReportSession.getMonth();
        int year = ReportSession.getYear();

        ClientHandler.getClient().requestReport(ReportType.SUBSCRIBERS, year, month, this);
    }
}
