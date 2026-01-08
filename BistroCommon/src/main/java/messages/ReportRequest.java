package messages;

import enums.ReportType;
import java.io.Serializable;

public class ReportRequest implements Serializable {
    private ReportType reportType;

    public ReportRequest(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
}
