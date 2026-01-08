package Controllers;

import enums.ReportType;

public class TimesReportController extends ReportController {
    @Override
    protected ReportType getReportType() {
        return ReportType.SCHEDULE;
    }
}