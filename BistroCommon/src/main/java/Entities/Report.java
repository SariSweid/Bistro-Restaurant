package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import enums.ReportType;

/**
 * Report represents a Report that is generated for the restaurant manager
 */
public class Report implements Serializable {
	private int reportID;
	private ReportType reportType;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDateTime generatedAt;
	private String content;
	
	/**
	 * constructor for new Report
	 * @param reportID
	 * @param reportType
	 * @param startDate
	 * @param endDate
	 * @param content
	 */
	public Report(int reportID, ReportType reportType, LocalDate startDate, LocalDate endDate, String content) {
		this.reportID = reportID;
		this.reportType = reportType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.generatedAt = LocalDateTime.now();
		this.content = content;
	}
	
	/**
	 * constructor for Report from db
	 * @param reportID
	 * @param reportType
	 * @param startDate
	 * @param endDate
	 * @param generatedAt
	 * @param content
	 */
	public Report(int reportID, ReportType reportType, LocalDate startDate, LocalDate endDate, LocalDateTime generatedAt, String content) {
		this.reportID = reportID;
		this.reportType = reportType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.generatedAt = LocalDateTime.now();
		this.content = content;
	}
	
	//getters, there are not setters because once the report is generated its immutable
	
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
