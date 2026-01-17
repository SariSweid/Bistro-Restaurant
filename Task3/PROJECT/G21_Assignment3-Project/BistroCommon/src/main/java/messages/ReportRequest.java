package messages;

import enums.ReportType;
import java.io.Serializable;

/**
 * Represents a request message to generate a specific management report.
 * This request specifies the type of report required and the time period (month and year) 
 * for which the data should be aggregated.
 */
@SuppressWarnings("serial")
public class ReportRequest implements Serializable {

    /**
     * The type of report to be generated (e.g., Revenue, Cancellations, Activity).
     */
    private final ReportType reportType;

    /**
     * The specific month for the report (1-12).
     */
    private final int month;

    /**
     * The specific year for the report.
     */
    private final int year;

    /**
     * Constructs a new ReportRequest with the specified type, month, and year.
     *
     * @param reportType the ReportType enum indicating which report to generate
     * @param month      the month for the report data
     * @param year       the year for the report data
     */
    public ReportRequest(ReportType reportType, int month, int year) {
        this.reportType = reportType;
        this.month = month;
        this.year = year;
    }

    /**
     * Returns the type of report associated with this request.
     *
     * @return the ReportType enum value
     */
    public ReportType getReportType() {
        return reportType;
    }

    /**
     * Returns the month specified for this report request.
     *
     * @return the month as an integer
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the year specified for this report request.
     *
     * @return the year as an integer
     */
    public int getYear() {
        return year;
    }
}