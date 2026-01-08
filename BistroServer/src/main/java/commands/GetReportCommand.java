package commands;

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
        try {
            if (!(data instanceof ReportRequest req)) return;

            ReportType type = req.getReportType();
            SubscribesReportController subcontroller = new SubscribesReportController();
            TimeReportController timecontroller = new  TimeReportController();
            LocalDate start = LocalDate.now().minusWeeks(4);
            LocalDate end = LocalDate.now();
            Report report;
            
            if (type == ReportType.SCHEDULE) {
                List<TimeData> timeData = timecontroller.generateReportData();
                report = Report.createTimeReport(0, type, start, end, timeData, "Time Report");
            } else {
                List<WeekData> weekData = subcontroller.generateReportData(type);
                report = Report.createSubscribersReport(0, type, start, end, weekData, "Monthly Subscribers Report");
            }

            ServerResponse res = new ServerResponse(true, report, "Report generated successfully");
            client.sendToClient(new Message(ActionType.GET_REPORT, res));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message(ActionType.GET_REPORT,
                        new ServerResponse(false, null, "Server error")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
