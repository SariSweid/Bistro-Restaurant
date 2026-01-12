package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import DB.DBController;
import Entities.WaitingListEntry;
import enums.ExitReason;
import enums.WaitingStatus;

/**
 * DAO for managing the waiting list.
 */
public class WaitingListDAO extends DBController {
	
	public WaitingListEntry getByConfirmationCode(int confirmationCode) {

	    String sql = "SELECT * FROM waitinglist WHERE confirmationCode = ?";

	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        pst.setInt(1, confirmationCode);
	        ResultSet rs = pst.executeQuery();

	        if (!rs.next()) return null;

	        Integer userId = rs.getObject("userID", Integer.class);
	        String email = rs.getString("Email");
	        String phone = rs.getString("Phone");
	        int numOfGuests = rs.getInt("numOfGuests");

	        LocalDate waitDate = rs.getDate("WaitDate").toLocalDate();
	        LocalTime waitTime = rs.getTime("WaitTime").toLocalTime();

	        String exitStr = rs.getString("exitReason");
	        ExitReason exitReason =
	                exitStr != null ? ExitReason.valueOf(exitStr) : null;

	        return new WaitingListEntry(
	                userId,
	                email,
	                phone,
	                numOfGuests,
	                confirmationCode,
	                waitDate,
	                waitTime,
	                exitReason
	        );

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
	 * Returns the first waiting list entry (FIFO) with no exit reason, ordered by date and time.
	 */
	public WaitingListEntry getNextWaitingEntry() {
		Connection con = getConnection();
	    try ( PreparedStatement pst = con.prepareStatement("SELECT * FROM waitinglist WHERE exitReason IS NULL ORDER BY WaitDate ASC, WaitTime ASC LIMIT 1")) {
	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            Integer userId = rs.getObject("userID", Integer.class);
	            String email = rs.getString("Email");
	            String phone = rs.getString("Phone");
	            int numOfGuests = rs.getInt("numOfGuests");
	            int confirmationCode = rs.getInt("confirmationCode");
	            LocalDate waitDate = rs.getDate("WaitDate").toLocalDate();
	            LocalTime waitTime = rs.getTime("WaitTime").toLocalTime();
	            return new WaitingListEntry(userId, email, phone, numOfGuests, confirmationCode, waitDate, waitTime, null);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public void updateExitReasonByConfirmationCode(
	        int confirmationCode,
	        ExitReason reason) {

	    Connection con = getConnection();


	    try (PreparedStatement pst = con.prepareStatement("UPDATE waitinglist SET exitReason = ? WHERE confirmationCode = ? AND exitReason IS NULL")) {
	        pst.setString(1, reason.name());
	        pst.setInt(2, confirmationCode);
	        pst.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	
	public int getWaitlistSubscriberCountBetween(LocalDate start, LocalDate end) {

	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement(
	            "SELECT COUNT(*) FROM waitinglist w JOIN user u ON w.userID = u.UserId WHERE u.Role = 'SUBSCRIBER' AND w.WaitDate BETWEEN ? AND ?")) {

	        pst.setDate(1, java.sql.Date.valueOf(start));
	        pst.setDate(2, java.sql.Date.valueOf(end));

	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}
	public List<WaitingListEntry> getWaitingEntriesWithoutExitReason() {

	    List<WaitingListEntry> list = new ArrayList<>();
	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement(
	            "SELECT * FROM waitinglist WHERE exitReason IS NULL")) {

	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {

	            WaitingListEntry entry = new WaitingListEntry(
	                    rs.getInt("userID"),
	                    rs.getString("Email"),
	                    rs.getString("Phone"),
	                    rs.getInt("numOfGuests"),
	                    rs.getInt("confirmationCode"),
	                    rs.getDate("WaitDate").toLocalDate(),
	                    rs.getTime("WaitTime").toLocalTime(),
	                    null,
	                    WaitingStatus.WAITING
	            );

	            list.add(entry);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}



	
	


	
	public boolean updateExitReason(int confirmationCode, ExitReason exitReason) {
	    String sql = "UPDATE waitinglist SET exitReason = ? WHERE confirmationCode = ?";
	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	         
	        if (exitReason != null) {
	            pst.setString(1, exitReason.name());
	        } else {
	            pst.setNull(1, java.sql.Types.VARCHAR);
	        }
	        
	        pst.setInt(2, confirmationCode);
	        return pst.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}



	
	/**
	 * Inserts a new WaitingListEntry into the database.
	 *
	 * @param w the WaitingListEntry to insert
	 * @return true if the insertion succeeded, false otherwise
	 */
	public Boolean addToWitingList(WaitingListEntry w) {

	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement(
	            "INSERT INTO waitinglist " +
	            "(userID, Email, Phone, numOfGuests, confirmationCode, " +
	            "WaitDate, WaitTime, enterdate, entertime, exitReason) " +
	            "VALUES (?,?,?,?,?,?,?,?,?,?)")) {


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

	        LocalDate nowDate = LocalDate.now();
	        LocalTime nowTime = LocalTime.now();

	        pst.setDate(8, java.sql.Date.valueOf(nowDate));
	        pst.setTime(9, java.sql.Time.valueOf(nowTime));

	
	        pst.setNull(10, java.sql.Types.VARCHAR);

	        int updateStatus = pst.executeUpdate();
	        return updateStatus > 0;

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
	public List<WaitingListEntry> getWaitingListBetweenDates(
	        LocalDate startDate,
	        LocalDate endDate) {

	    Connection con = getConnection();
	    List<WaitingListEntry> result = new ArrayList<>();

	    try (PreparedStatement pst = con.prepareStatement(
	            "SELECT * FROM waitinglist " +
	            "WHERE WaitDate BETWEEN ? AND ? " +
	            "ORDER BY WaitDate, WaitTime")) {

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
	            LocalTime waitTime =
	                    rs.getTime("WaitTime") != null
	                    ? rs.getTime("WaitTime").toLocalTime()
	                    : null;

	            String exitStr = rs.getString("exitReason");
	            ExitReason exitReason =
	                    exitStr != null ? ExitReason.valueOf(exitStr) : null;

	            WaitingStatus status =
	                    WaitingStatus.valueOf(rs.getString("status"));

	            WaitingListEntry entry = new WaitingListEntry(
	                    userId,
	                    email,
	                    phone,
	                    numOfGuests,
	                    confirmationCode,
	                    waitDate,
	                    waitTime,
	                    exitReason,
	                    status
	            );

	            result.add(entry);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return result;
	}



	public WaitingListEntry findExistingEntry(Integer userID, LocalDate date, LocalTime time) {
	    String sql = "SELECT * FROM waitinglist WHERE userID = ? AND WaitDate = ? AND WaitTime = ? AND exitReason IS NULL";

	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        pst.setInt(1, userID);
	        pst.setDate(2, java.sql.Date.valueOf(date));
	        pst.setTime(3, java.sql.Time.valueOf(time));

	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            return new WaitingListEntry(
	                rs.getObject("userID", Integer.class),
	                rs.getString("Email"),
	                rs.getString("Phone"),
	                rs.getInt("numOfGuests"),
	                rs.getInt("confirmationCode"),
	                rs.getDate("WaitDate").toLocalDate(),
	                rs.getTime("WaitTime").toLocalTime(),
	                rs.getString("exitReason") == null ? null : ExitReason.valueOf(rs.getString("exitReason")),
	                WaitingStatus.valueOf(rs.getString("status"))
	            );
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
	}



}
