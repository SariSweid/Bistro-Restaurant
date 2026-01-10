package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DB.DBController;
import Entities.WaitingListEntry;
import enums.ExitReason;

/**
 * DAO for managing the waiting list.
 */
public class WaitingListDAO extends DBController {
	
	
	public Boolean updateExitReason(int confirmationCode, ExitReason exitReason) {

	    Connection con = getConnection(); // connect to DB

	    try (PreparedStatement pst = con.prepareStatement(
	            "UPDATE `waitinglist` SET exitReason = ? WHERE confirmationCode = ?")) {

	        pst.setString(1, exitReason.name());
	        pst.setInt(2, confirmationCode);

	        int rows = pst.executeUpdate();
	        return rows > 0;

	    } catch (SQLException e) {
	        System.err.println("SQL Exception during update: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}


	
    /**
     * Inserts a new WaitingListEntry into the database.
     *
     * @param w the Reservation to insert
     * @return true if the insertion succeeded, false otherwise
     */
	public Boolean addToWitingList(WaitingListEntry w) {

	    Connection con = getConnection(); // connect to DB

	    try (PreparedStatement pst = con.prepareStatement(
	            "INSERT INTO `waitinglist` (userID, Email, Phone, numOfGuests, confirmationCode, WaitDate, WaitTime, exitReason) VALUES (?,?,?,?,?,?,?,?)")) {

	        if (w.getUserID() == null)
	            pst.setNull(1, java.sql.Types.INTEGER);
	        else
	            pst.setInt(1, w.getUserID());

	        pst.setString(2, w.getEmail());
	        pst.setString(3, w.getPhone());
	        pst.setInt(4, w.getNumOfGuests());
	        pst.setInt(5, w.getConfirmationCode());
	        pst.setDate(6, java.sql.Date.valueOf(w.getWaitDate()));
	        pst.setTime(7, java.sql.Time.valueOf(w.getWaitTime()));
	        pst.setString(8, w.getExitReason().name());

	        int update_status = pst.executeUpdate();
	        return update_status > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	/**
	 * Returns a list of waiting list entries between two given dates, ordered by date and time.
	 *
	 * @param startDate the start date (inclusive)
	 * @param endDate the end date (inclusive)
	 * @return a list of WaitingListEntry objects within the specified date range
	 */
	public List<WaitingListEntry> getWaitingListBetweenDates(LocalDate startDate, LocalDate endDate) {
	    Connection con = getConnection();
	    List<WaitingListEntry> result = new ArrayList<>();

	    try (PreparedStatement pst = con.prepareStatement(
	            "SELECT * FROM waitinglist WHERE WaitDate BETWEEN ? AND ? ORDER BY WaitDate, WaitTime")) {

	        pst.setDate(1, java.sql.Date.valueOf(startDate));
	        pst.setDate(2, java.sql.Date.valueOf(endDate));

	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	            Integer userId = rs.getObject("userID", Integer.class);
	            String email = rs.getString("Email");
	            String phone = rs.getString("Phone");
	            int numOfGuests = rs.getInt("numOfGuests");
	            int confirmationCode = rs.getInt("confirmationCode");

	            LocalDate waitDate = rs.getDate("WaitDate").toLocalDate();
	            java.time.LocalTime waitTime = rs.getTime("WaitTime") != null ? rs.getTime("WaitTime").toLocalTime() : null;

	            String exitStr = rs.getString("exitReason");
	            ExitReason exitReason = exitStr != null ? ExitReason.valueOf(exitStr) : null;

	            WaitingListEntry entry = new WaitingListEntry(
	                    userId, email, phone, numOfGuests, confirmationCode, waitDate, waitTime, exitReason
	            );

	            result.add(entry);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return result;
	}







}
