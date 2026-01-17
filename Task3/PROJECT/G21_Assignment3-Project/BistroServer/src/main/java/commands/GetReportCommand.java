package commands;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import Entities.Report;
import Entities.TimeData;
import Entities.WeekData;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import enums.ReportType;

import logicControllers.SubscribesReportController;
import logicControllers.TimeReportController;
import messages.ReportRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for generating restaurant activity reports.
 * This class handles requests for different types of reports (Schedule/Time or Subscribers)
 * for a specific month and year, delegating data collection to the respective controllers.
 */
public class GetReportCommand implements Command {

    /**
     * Executes the report generation logic.
     * Extracts the date range and report type from the ReportRequest, invokes the 
     * appropriate controller to gather data, and returns a populated Report object 
     * within a ServerResponse to the client.
     *
     * @param data   the ReportRequest containing the month, year, and ReportType
     * @param client the connection to the client that requested the report
     */
	@Override
	public void execute(Object data, ConnectionToClient client) {
	    if (!(data instanceof ReportRequest req)) return;

	    int month = req.getMonth();
	    int year = req.getYear();
	    ReportType type = req.getReportType();

	    // Calculate the date range for the requested month
	    LocalDate start = LocalDate.of(year, month, 1);
	    LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

	    Report report;

	    // Branching logic based on the requested report type
	    if (type == ReportType.SCHEDULE) {
	        // Generate time-based schedule report
	        List<TimeData> timeData = new TimeReportController().generateReportData(start, end);
	        report = Report.createTimeReport(0, type, start, end, timeData, "Time Report");
	    } else {
	        // Generate subscriber-based activity report
	        List<WeekData> weekData = new SubscribesReportController().generateReportData(start, end);
	        report = Report.createSubscribersReport(0, type, start, end, weekData, "Subscribers Report");
	    }


	    try {
	        // Send the generated report back to the client
			client.sendToClient(new Message(ActionType.GET_REPORT,
			    new ServerResponse(true, report, "Report generated successfully")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}