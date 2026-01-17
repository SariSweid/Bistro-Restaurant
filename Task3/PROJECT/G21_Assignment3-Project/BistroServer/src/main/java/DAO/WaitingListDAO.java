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
 * Data Access Object (DAO) for managing the restaurant's waiting list.
 * This class handles operations for WaitingListEntry, including FIFO retrieval,
 * tracking entry/exit timestamps, and managing exit reasons.
 * Extends DBController to manage database connectivity.
 */
public class WaitingListDAO extends DBController {
	
    /**
     * Retrieves a waiting list entry based on its unique confirmation code.
     *
     * @param confirmationCode the unique code assigned to the entry.
     * @return a WaitingListEntry if found, null otherwise.
     */
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
	                userId, email, phone, numOfGuests, confirmationCode,
	                waitDate, waitTime, exitReason
	        );

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
	 * Returns the next customer in line (FIFO - First In, First Out).
     * Retrieves the oldest entry that has not yet exited the waiting list.
	 *
	 * @return the oldest active WaitingListEntry, or null if the list is empty.
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
	
    /**
     * Updates the exit reason for an entry that is currently active.
     * Used when a customer is seated or decides to leave the list.
     *
     * @param confirmationCode the entry identifier.
     * @param reason the ExitReason status.
     */
	public void updateExitReasonByConfirmationCode(int confirmationCode, ExitReason reason) {
	    Connection con = getConnection();
	    try (PreparedStatement pst = con.prepareStatement("UPDATE waitinglist SET exitReason = ? WHERE confirmationCode = ? AND exitReason IS NULL")) {
	        pst.setString(1, reason.name());
	        pst.setInt(2, confirmationCode);
	        pst.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
    /**
     * Counts how many registered subscribers used the waiting list within a specific date range.
     * Useful for marketing and activity reports.
     *
     * @param start start date of the range.
     * @param end end date of the range.
     * @return count of subscriber entries.
     */
	public int getWaitlistSubscriberCountBetween(LocalDate start, LocalDate end) {
	    Connection con = getConnection();
	    try (PreparedStatement pst = con.prepareStatement(
	            "SELECT COUNT(*) FROM waitinglist w JOIN user u ON w.userID = u.UserId WHERE u.Role = 'SUBSCRIBER' AND w.WaitDate BETWEEN ? AND ?")) {
	        pst.setDate(1, java.sql.Date.valueOf(start));
	        pst.setDate(2, java.sql.Date.valueOf(end));
	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) return rs.getInt(1);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}

    /**
     * Retrieves all active entries currently waiting for a table.
     *
     * @return a list of active WaitingListEntry objects.
     */
	public List<WaitingListEntry> getWaitingEntriesWithoutExitReason() {
	    List<WaitingListEntry> list = new ArrayList<>();
	    Connection con = getConnection();
	    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM waitinglist WHERE exitReason IS NULL")) {
	        ResultSet rs = pst.executeQuery();
	        while (rs.next()) {
	            list.add(new WaitingListEntry(
	                    rs.getInt("userID"),
	                    rs.getString("Email"),
	                    rs.getString("Phone"),
	                    rs.getInt("numOfGuests"),
	                    rs.getInt("confirmationCode"),
	                    rs.getDate("WaitDate").toLocalDate(),
	                    rs.getTime("WaitTime").toLocalTime(),
	                    null,
	                    WaitingStatus.WAITING
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

    /**
     * Updates or clears the exit reason for a specific entry.
     *
     * @param confirmationCode the entry identifier.
     * @param exitReason the new reason (or null to clear).
     * @return true if successful.
     */
	public boolean updateExitReason(int confirmationCode, ExitReason exitReason) {
	    String sql = "UPDATE waitinglist SET exitReason = ? WHERE confirmationCode = ?";
	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	        if (exitReason != null) pst.setString(1, exitReason.name());
	        else pst.setNull(1, java.sql.Types.VARCHAR);
	        pst.setInt(2, confirmationCode);
	        return pst.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * Adds a new customer to the waiting list.
     * Automatically captures the current system date and time as the entry timestamp.
	 *
	 * @param w the entry details.
	 * @return true if added successfully.
	 */
	public Boolean addToWitingList(WaitingListEntry w) {
	    Connection con = getConnection();
	    try (PreparedStatement pst = con.prepareStatement(
	            "INSERT INTO waitinglist (userID, Email, Phone, numOfGuests, confirmationCode, WaitDate, WaitTime, enterdate, entertime, exitReason) VALUES (?,?,?,?,?,?,?,?,?,?)")) {

	        if (w.getUserID() == null) pst.setNull(1, java.sql.Types.INTEGER);
	        else pst.setInt(1, w.getUserID());

	        pst.setString(2, w.getEmail());
	        pst.setString(3, w.getPhone());
	        pst.setInt(4, w.getNumOfGuests());
	        pst.setInt(5, w.getConfirmationCode());
	        pst.setDate(6, java.sql.Date.valueOf(w.getWaitDate()));
	        pst.setTime(7, java.sql.Time.valueOf(w.getWaitTime()));

	        pst.setDate(8, java.sql.Date.valueOf(LocalDate.now()));
	        pst.setTime(9, java.sql.Time.valueOf(LocalTime.now()));
	        pst.setNull(10, java.sql.Types.VARCHAR);

	        return pst.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
	 * Fetches history of waiting list entries within a range, ordered chronologically.
	 *
	 * @param startDate the start of the reporting period.
	 * @param endDate the end of the reporting period.
	 * @return a list of historical entries.
	 */
	public List<WaitingListEntry> getWaitingListBetweenDates(LocalDate startDate, LocalDate endDate) {
	    Connection con = getConnection();
	    List<WaitingListEntry> result = new ArrayList<>();
	    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM waitinglist WHERE WaitDate BETWEEN ? AND ? ORDER BY WaitDate, WaitTime")) {
	        pst.setDate(1, java.sql.Date.valueOf(startDate));
	        pst.setDate(2, java.sql.Date.valueOf(endDate));
	        ResultSet rs = pst.executeQuery();
	        while (rs.next()) {
	            String exitStr = rs.getString("exitReason");
	            result.add(new WaitingListEntry(
	                    rs.getObject("userID", Integer.class),
	                    rs.getString("Email"), rs.getString("Phone"),
	                    rs.getInt("numOfGuests"), rs.getInt("confirmationCode"),
	                    rs.getDate("WaitDate").toLocalDate(),
	                    rs.getTime("WaitTime") != null ? rs.getTime("WaitTime").toLocalTime() : null,
	                    exitStr != null ? ExitReason.valueOf(exitStr) : null
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return result;
	}

    /**
     * Checks if a specific registered user already has an active spot in the waiting list.
     *
     * @param userID the ID of the registered user.
     * @param date requested date.
     * @param time requested time slot.
     * @return the existing WaitingListEntry if found, null otherwise.
     */
	public WaitingListEntry findExistingEntry(Integer userID, LocalDate date, LocalTime time) {
	    String sql = "SELECT * FROM waitinglist WHERE userID = ? AND WaitDate = ? AND WaitTime = ? AND exitReason IS NULL";
	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	        pst.setInt(1, userID);
	        pst.setDate(2, java.sql.Date.valueOf(date));
	        pst.setTime(3, java.sql.Time.valueOf(time));
	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            String exitStr = rs.getString("exitReason");
	            return new WaitingListEntry(
	                rs.getInt("userID"), rs.getString("Email"), rs.getString("Phone"),
	                rs.getInt("numOfGuests"), rs.getInt("confirmationCode"),
	                rs.getDate("WaitDate").toLocalDate(), rs.getTime("WaitTime").toLocalTime(),
	                exitStr != null ? ExitReason.valueOf(exitStr) : null
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
    /**
     * Searches for an active waiting list entry using contact details (Email or Phone).
     * Used for identifying Guests (non-registered users) in the list.
     *
     * @param email contact email.
     * @param phone contact phone.
     * @param date requested date.
     * @param time requested time slot.
     * @return an existing WaitingListEntry if match found, null otherwise.
     */
	public WaitingListEntry findExistingEntryByContact(String email, String phone, LocalDate date, LocalTime time) {
	    String sql = "SELECT * FROM waitinglist WHERE WaitDate = ? AND WaitTime = ? AND exitReason IS NULL";
	    boolean hasEmail = email != null && !email.isBlank();
	    boolean hasPhone = phone != null && !phone.isBlank();

	    if (hasEmail && hasPhone) sql += " AND (Email = ? OR Phone = ?)";
	    else if (hasEmail) sql += " AND Email = ?";
	    else if (hasPhone) sql += " AND Phone = ?";
	    else return null; 

	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	        pst.setDate(1, java.sql.Date.valueOf(date));
	        pst.setTime(2, java.sql.Time.valueOf(time));
	        int paramIndex = 3;
	        if (hasEmail) pst.setString(paramIndex++, email);
	        if (hasPhone) pst.setString(paramIndex++, phone);

	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            String exitStr = rs.getString("exitReason");
	            return new WaitingListEntry(
	                rs.getObject("userID", Integer.class), rs.getString("Email"), rs.getString("Phone"),
	                rs.getInt("numOfGuests"), rs.getInt("confirmationCode"),
	                rs.getDate("WaitDate").toLocalDate(), rs.getTime("WaitTime").toLocalTime(),
	                exitStr == null ? null : ExitReason.valueOf(exitStr)
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}