package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import enums.ReportType;

@SuppressWarnings("serial")
public class Report implements Serializable {
    private int reportID;
    private ReportType reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime generatedAt;
    private String content;

    private List<WeekData> weekData;  
    private List<TimeData> timeData;   

    private Report(int reportID, ReportType reportType, LocalDate startDate, LocalDate endDate, List<WeekData> weekData, List<TimeData> timeData, String content) {
        this.reportID = reportID;
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.generatedAt = LocalDateTime.now();
        this.weekData = weekData;
        this.timeData = timeData;
        this.content = content;
    }

    // SUBSCRIBERS
    public static Report createSubscribersReport(int reportID, ReportType type, LocalDate start, LocalDate end, List<WeekData> weekData, String content) {
        return new Report(reportID, type, start, end, weekData, null, content);
    }

    // TIME
    public static Report createTimeReport(int reportID, ReportType type, LocalDate start, LocalDate end, List<TimeData> timeData, String content) {
        return new Report(reportID, type, start, end, null, timeData, content);
    }

    public int getReportID() { return reportID; }
    public ReportType getReportType() { return reportType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public String getContent() { return content; }
    public List<WeekData> getWeekData() { return weekData; }  
    public List<TimeData> getTimeData() { return timeData; } 
}
