package Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import enums.ReportType;

public class Report {
	private int reportID;
	private ReportType reportType;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDateTime generatedAt;
	private String content;
	
	//constructor for new Report
	public Report(int reportID, ReportType reportType, LocalDate startDate, LocalDate endDate, String content) {
		this.reportID = reportID;
		this.reportType = reportType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.generatedAt = LocalDateTime.now();
		this.content = content;
	}
	
	//constructor for Report from db
	public Report(int reportID, ReportType reportType, LocalDate startDate, LocalDate endDate, LocalDateTime generatedAt, String content) {
		this.reportID = reportID;
		this.reportType = reportType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.generatedAt = LocalDateTime.now();
		this.content = content;
	}
	
	//getters
	
	public int getReportID() {
		return this.reportID;
	}
	
	public ReportType getReportType() {
		return this.reportType;
	}
	
	public LocalDate getStartDate() {
		return this.startDate;
	}
	
	public LocalDate getEndDate() {
		return this.endDate;
	}
	
	public LocalDateTime getGeneratedAt() {
		return this.generatedAt;
	}
	
	public String getContent() {
		return this.content;
	}
}
