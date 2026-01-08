package commands;

import Entities.Report;
import Entities.WeekData;
import enums.ReportType;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import messages.ReportRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;
import logicControllers.WeeklyReportGenerator;
import java.time.LocalDate;
import java.util.List;

public class GetReportCommand implements Command {

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            if (!(data instanceof ReportRequest req)) return;

            ReportType type = req.getReportType();

            WeeklyReportGenerator generator = new WeeklyReportGenerator();
            List<WeekData> weekData = generator.generateReportData(type);

            LocalDate start = LocalDate.now().minusWeeks(4);
            LocalDate end = LocalDate.now();
            String content = "Report generated from database";

            Report report = new Report(0, type, start, end, weekData, content);

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
