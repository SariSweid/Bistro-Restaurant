package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import DB.DBController;
import Entities.Report;

/**
 * Data Access Object (DAO) for managing Report entities in the database.
 * This class handles the persistence of various system reports (e.g., Activity, Revenue)
 * and provides methods to store generated report metadata and content.
 */
public class ReportDAO extends DBController {

    /**
     * Inserts a new report record into the database.
     * This method maps the fields of a Report object, including its type, 
     * date range, and the timestamp of generation, to the corresponding columns 
     * in the report table.
     *
     * @param r the Report entity to be inserted into the database.
     * @return true if the insertion was successful (at least one row affected), 
     * false otherwise or if an SQLException occurred.
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
     * Note: This method is currently commented out in the source code. 
     * When active, it fetches the full history of generated reports, converting 
     * SQL types (Date, Timestamp) back to Java Time API types (LocalDate, LocalDateTime).
     *
     * @return a list of all Report objects stored in the system.
     */
/* public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM `report`")) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                reports.add(new Report(
                        rs.getInt("Report_Id"),
                        ReportType.valueOf(rs.getString("Type")),
                        rs.getDate("From").toLocalDate(),
                        rs.getDate("To").toLocalDate(),
                        rs.getTimestamp("generatedAt").toLocalDateTime(),
                        rs.getString("content")
                ));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return reports;
    }
*/
}