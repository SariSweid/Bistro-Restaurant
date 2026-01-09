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

public class GetReportCommand implements Command {

	@Override
	public void execute(Object data, ConnectionToClient client) {
	    if (!(data instanceof ReportRequest req)) return;

	    int month = req.getMonth();
	    int year = req.getYear();
	    ReportType type = req.getReportType();

	    LocalDate start = LocalDate.of(year, month, 1);
	    LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

	    Report report;

	    if (type == ReportType.SCHEDULE) {
	        List<TimeData> timeData = new TimeReportController().generateReportData(start, end);
	        report = Report.createTimeReport(0, type, start, end, timeData, "Time Report");
	    } else {
	        List<WeekData> weekData = new SubscribesReportController().generateReportData(start, end);
	        report = Report.createSubscribersReport(0, type, start, end, weekData, "Subscribers Report");
	    }


	    try {
			client.sendToClient(new Message(ActionType.GET_REPORT,
			    new ServerResponse(true, report, "Report generated successfully")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
