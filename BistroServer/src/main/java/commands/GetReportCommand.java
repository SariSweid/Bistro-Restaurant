package commands;

import Entities.Report;
import enums.ReportType;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import messages.ReportRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetReportCommand implements Command {

    @Override
    public void execute(Object data, ConnectionToClient client) {
    	
    	System.out.println("IM HERE HELLO ");
        try {
            if (!(data instanceof ReportRequest req)) return;
            

            ReportType type = req.getReportType();

          //  Report report = generateReport(type);

          //  ServerResponse res = new ServerResponse(true, report, "Report generated successfully");

          //  client.sendToClient(new Message(ActionType.GET_REPORT, res));

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

    private void generateReport(ReportType type) {
      //  List<WeekData> weekData = new ArrayList<>();

        if (type == ReportType.SCHEDULE) {
            for (int i = 1; i <= 4; i++) {
            //    weekData.add(new WeekData("Week " + i, 0, i * 5));
            }
        } else if (type == ReportType.SUBSCRIBERS) {
            for (int i = 1; i <= 4; i++) {
              //  weekData.add(new WeekData("Week " + i, i * 10, 0));
            }
        }

        LocalDate start = LocalDate.now().minusWeeks(4);
        LocalDate end = LocalDate.now();
        String content = "Auto-generated report";

      //  return new Report(0, type, start, end, weekData, content);
    }
}
