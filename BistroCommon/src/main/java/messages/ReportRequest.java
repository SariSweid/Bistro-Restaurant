package messages;

import enums.ReportType;
import java.io.Serializable;

@SuppressWarnings("serial")
public class ReportRequest implements Serializable {
    private final ReportType reportType;
    private final int month;
    private final int year;

    public ReportRequest(ReportType reportType, int month, int year) {
        this.reportType = reportType;
        this.month = month;
        this.year = year;
    }

    public ReportType getReportType() { return reportType; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
}
