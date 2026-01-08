package Controllers;

import enums.ReportType;

public class SubscribersReportController extends ReportController {
    @Override
    protected ReportType getReportType() {
        return ReportType.SUBSCRIBERS;
    }
}