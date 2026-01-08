package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import enums.ReportType;

public class Report implements Serializable {
    private int reportID;
    private ReportType reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime generatedAt;//
    private String content;
    private List<WeekData> weekData;  

   
    public Report(int reportID, ReportType reportType, LocalDate startDate, LocalDate endDate, List<WeekData> weekData, String content) {
        this.reportID = reportID;
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.generatedAt = LocalDateTime.now();
        this.weekData = weekData;
        this.content = content;
    }

    
    public Report(int reportID, ReportType reportType, LocalDate startDate, LocalDate endDate, LocalDateTime generatedAt, List<WeekData> weekData, String content) {
        this.reportID = reportID;
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.generatedAt = generatedAt;
        this.weekData = weekData;
        this.content = content;
    }

    public int getReportID() { return reportID; }
    public ReportType getReportType() { return reportType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public String getContent() { return content; }
    public List<WeekData> getWeekData() { return weekData; }  
}
