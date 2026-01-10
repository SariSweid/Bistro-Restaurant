package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import DB.DBController;
import Entities.Report;
import enums.ReportType;

/**
 * DAO for managing reports.
 */
public class ReportDAO extends DBController {

    /**
     * Inserts a new report into the database.
     *
     * @param r the report to insert
     * @return true if the insertion succeeded, false otherwise
     */
    public boolean addReport(Report r) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO `report` (Report_Id, Type, `From`, `To`, generatedAt, content) VALUES (?,?,?,?,?,?)")) {

            pst.setInt(1, r.getReportID());
            pst.setString(2, r.getReportType().name());
            pst.setDate(3, Date.valueOf(r.getStartDate()));
            pst.setDate(4, Date.valueOf(r.getEndDate()));
            pst.setTimestamp(5, Timestamp.valueOf(r.getGeneratedAt()));
            pst.setString(6, r.getContent());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all reports from the database.
     *
     * @return a list of reports
     */
//    public List<Report> getAllReports() {
//        List<Report> reports = new ArrayList<>();
//        try (Connection con = getConnection();
//             PreparedStatement pst = con.prepareStatement("SELECT * FROM `report`")) {
//
//            ResultSet rs = pst.executeQuery();
//            while (rs.next()) {
//                reports.add(new Report(
//                        rs.getInt("Report_Id"),
//                        ReportType.valueOf(rs.getString("Type")),
//                        rs.getDate("From").toLocalDate(),
//                        rs.getDate("To").toLocalDate(),
//                        rs.getTimestamp("generatedAt").toLocalDateTime(),
//                        rs.getString("content")
//                ));
//            }
//
//        } catch (SQLException e) { e.printStackTrace(); }
//        return reports;
//    }
}
