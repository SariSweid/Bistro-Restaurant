package handlers;

import Controllers.ReportController;
import Entities.Report;
import enums.ReportType;
import common.ServerResponse;
import javafx.scene.chart.XYChart;

public class ReportHandler implements ResponseHandler {

    private ReportController controller;

    
    public void setController(ReportController controller) {
        this.controller = controller;
    }

    public ReportHandler(ReportController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(Object data) {
        ServerResponse response = (ServerResponse) data;


        if (!response.isSuccess()) {
            controller.showError(response.getMessage());
            return;
        }

        
        Report report = (Report) response.getData();

        if (report.getReportType() == ReportType.SCHEDULE) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Average Delay");

            
            //report.getWeekData().forEach(w -> 
                //series.getData().add(new XYChart.Data<>(w.getName(), w.getDelay()))
          //  );

            controller.showLineChart(series, "Monthly Time Report");

        } else if (report.getReportType() == ReportType.SUBSCRIBERS) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Orders per Week");

         //   report.getWeekData().forEach(w -> 
         //       series.getData().add(new XYChart.Data<>(w.getName(), w.getOrders()))
         //   );

            controller.showBarChart(series, "Monthly Subscribers Report");
        }
    }
}
