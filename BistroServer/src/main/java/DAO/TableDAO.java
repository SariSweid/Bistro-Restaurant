package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import DB.DBController;
import Entities.Reservation;
import Entities.Table;

/**
 * Data Access Object (DAO) for managing Table entities and their persistence logic.
 * This class provides CRUD operations for physical tables in the restaurant and includes 
 * advanced logic for finding available tables based on timestamps, seating capacity, 
 * and existing reservation overlaps.
 */
public class TableDAO extends DBController {

    /**
     * Updates the general availability status of a table in the database.
     *
     * @param tableId     the unique identifier of the table.
     * @param isAvailable true to mark the table as free, false to mark it as occupied.
     * @return true if the database update was successful, false otherwise.
     */
    public boolean updateTableIsAvailable(int tableId, boolean isAvailable) {
        try(Connection con = getConnection();
        		PreparedStatement pst = con.prepareStatement(
                "UPDATE `table` SET IsAvailable = ? WHERE TableId = ?"
        )) {
            pst.setBoolean(1, isAvailable);
            pst.setInt(2, tableId);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Inserts a new table record into the database.
     *
     * @param tableId the unique ID for the new table.
     * @param seats   the seating capacity of the table.
     * @return true if the table was successfully created, false otherwise.
     */
	public boolean insertTable(int tableId, int seats) {
	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement(
	            "INSERT INTO `table` (TableId, Capacity, IsAvailable) VALUES (?, ?, ?)")) {

	        pst.setInt(1, tableId);
	        pst.setInt(2, seats);
	        pst.setBoolean(3, true);

	        return pst.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
    /**
     * Updates the seating capacity of an existing table.
     *
     * @param tableId the ID of the table to update.
     * @param seats   the new seating capacity.
     * @return true if the update was successful.
     */
	public boolean updateTable(int tableId, int seats) {
	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement(
	            "UPDATE `table` SET Capacity = ? WHERE TableId = ?")) {

	        pst.setInt(1, seats);
	        pst.setInt(2, tableId);

	        return pst.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
    /**
     * Deletes a table record from the database by its ID.
     *
     * @param tableId the unique identifier of the table to be removed.
     * @return true if the deletion was successful.
     */
	public boolean deleteTable(int tableId) {
	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement(
	            "DELETE FROM `table` WHERE TableId = ?")) {

	        pst.setInt(1, tableId);
	        return pst.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
    /**
     * Retrieves a specific table's details by its ID.
     *
     * @param tableid the table ID to look for.
     * @return the Table entity if found, or null otherwise.
     */
	public Table GetTable(int tableid) {
		
		Connection con = getConnection();
		Table t = null;
		
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `table` WHERE TableId = ?")){
			pst.setInt(1, tableid);
			ResultSet rs = pst.executeQuery();
			
	        if (rs.next()) {
	        	int tableId = rs.getInt("TableId");
	        	int capacity = rs.getInt("Capacity");
	        	Boolean isAvailable = rs.getBoolean("IsAvailable");	        	
	        	t = new Table(tableId , capacity , isAvailable );
		        }
    	        
		}
			
	catch (SQLException e) {
		e.printStackTrace();
	}

	return t;
	}

    /**
     * Retrieves a list of all tables registered in the restaurant database.
     *
     * @return a List containing all Table entities.
     */
	public List<Table> GetAllTables(){
		
		Connection con = getConnection();
    	List<Table> tables = new ArrayList<>();
    	
    	try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `table`")){
    		
    		ResultSet rs = pst.executeQuery();
    		
    		while(rs.next()) {
			
    			int tableId  = rs.getInt("TableId");
    			int capacity  = rs.getInt("Capacity"); 
    			Boolean isavailable = rs.getBoolean("IsAvailable");
    			Table t = new Table(tableId , capacity , isavailable );
    			tables.add(t);
    		
    	}
    		
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	 return tables;		
	}

    /**
     * Updates an existing table's availability status using a Table object.
     *
     * @param t the Table object containing updated availability.
     * @return true if the update succeeded, false otherwise.
     */
	public Boolean UpdateTable(Table t) {
		
		Connection con = getConnection();
		
		try (PreparedStatement pst = con.prepareStatement("UPDATE `table` SET IsAvailable = ?   WHERE TableId = ? ")){
			
			pst.setBoolean(1, t.isAvailable()); 
			pst.setInt(2, t.getTableID()); 
			
            int rows = pst.executeUpdate();

            return rows > 0; 
		
        } catch (SQLException e) {
        	System.err.println("SQL Exception during update: " + e.getMessage());
        	e.printStackTrace();
        	return false; 
        }			
	}
		
    /**
     * Removes an existing table from the database using a Table object reference.
     *
     * @param t the Table object to be deleted.
     * @return true if the deletion succeeded, false otherwise.
     */
	public Boolean DeleteTable(Table t) {
		
		Connection con = getConnection();
		
		try (PreparedStatement pst = con.prepareStatement("DELETE FROM `table` WHERE TableId = ? ")){
			
			pst.setInt(1, t.getTableID()); 
			
            int rows = pst.executeUpdate();
            
            return rows > 0; 
			
        } catch (SQLException e) {
        	System.err.println("SQL Exception during update: " + e.getMessage());
        	e.printStackTrace();
        	return false; 
        }			
	}

	/**
	 * Finds the best available table for a specific date, time, and guest count.
	 * The logic ensures the table has sufficient capacity and is not currently reserved 
	 * within a 2-hour window (120 minutes) of the requested time.
	 *
	 * @param numOfGuests the number of guests in the party.
	 * @param date        the requested date for the reservation.
	 * @param time        the requested time for the reservation.
	 * @return the smallest suitable Table if available, or null if fully booked.
	 */
	public Table getAvailableTableAtTime(int numOfGuests, LocalDate date, LocalTime time) {
	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement(
	        "SELECT t.TableId, t.Capacity, t.IsAvailable " +
	        "FROM `table` t " +
	        "WHERE t.Capacity >= ? " +
	        "  AND t.IsAvailable = 1 " +
	        "  AND t.TableId NOT IN ( " +
	        "        SELECT r.TableId " +
	        "        FROM reservation r " +
	        "        WHERE r.reservationDate = ? " +
	        "          AND r.status IN ('PENDING','CONFIRMED','SEATED') " +
	        "          AND ABS(TIMESTAMPDIFF(MINUTE, r.reservationTime, ?)) < 120 " +
	        "  ) " +
	        "ORDER BY t.Capacity ASC " +
	        "LIMIT 1"
	    )) {

	        pst.setInt(1, numOfGuests);
	        pst.setDate(2, java.sql.Date.valueOf(date));
	        pst.setTime(3, java.sql.Time.valueOf(time));

	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            int tableId = rs.getInt("TableId");
	            int capacity = rs.getInt("Capacity");
	            boolean isAvailable = rs.getBoolean("IsAvailable");
	            return new Table(tableId, capacity, isAvailable);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
	}

	/**
	 * Frees a table assigned to a specific reservation and completes the reservation status.
	 * This is typically called when a customer pays or leaves the restaurant.
	 *
	 * @param reservationId the ID of the reservation whose table should be released.
	 * @return true if the table was freed and reservation status updated successfully.
	 */
	public boolean updateTableIsFree(int reservationId) {
	    Connection con = getConnection();
	    try {
	        Reservation r = GetReservation(reservationId);
	        if (r == null || r.getTableID() == null) return false;

	        try (PreparedStatement pstTable = con.prepareStatement(
	                "UPDATE `table` SET IsAvailable = 1 WHERE TableId = ?")) {
	            pstTable.setInt(1, r.getTableID());
	            pstTable.executeUpdate();
	        }

	        try (PreparedStatement pstReservation = con.prepareStatement(
	                "UPDATE reservation SET TableId = ?, status = ? WHERE reservationID = ?")) {
	            pstReservation.setNull(1, java.sql.Types.INTEGER);
	            pstReservation.setString(2, "COMPLETED");
	            pstReservation.setInt(3, reservationId);
	            int updateStatus = pstReservation.executeUpdate();
	            return updateStatus > 0;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
    /**
     * Finds a currently available table in the restaurant that best fits the guest count.
     * "Best fit" refers to the table with the smallest capacity that can still hold the party.
     *
     * @param guests the number of guests.
     * @return a Table object if one is found, otherwise null.
     */
    public Table findAvailableTable(int guests) {
        Table bestFit = null;
        try {
            List<Table> tables = this.GetAllTables();

            for (Table t : tables) {
                
                if (t.isAvailable() && t.getCapacity() >= guests) {
                    
                    if (bestFit == null || t.getCapacity() < bestFit.getCapacity()) {
                        bestFit = t;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bestFit; 
    }
	
	/**
	 * Retrieves a full Reservation entity by its unique ID.
	 * Includes conversion of SQL Date/Time types to LocalDate and LocalTime.
	 *
	 * @param ReservationId the unique identifier of the reservation.
	 * @return the Reservation object if found, otherwise null.
	 */
	public Reservation GetReservation(int ReservationId) {
		
		Connection con = getConnection();
		Reservation r = null;
		
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `reservation` WHERE reservationID = ?")){
			pst.setInt(1, ReservationId);
			ResultSet rs = pst.executeQuery();
			
	        if (rs.next()) {
	        	
    			int reservationID  = rs.getInt("reservationID");
    			Date reservationDate  = rs.getDate("reservationDate"); 
    			Time reservationTime  = rs.getTime("reservationTime");
    			int numOfGuests  = rs.getInt("numOfGuests");
    			int confirmation_code = rs.getInt("confirmationCode");
    			enums.ReservationStatus status = enums.ReservationStatus.valueOf(rs.getString("status"));
    			int customerID  = rs.getInt("customerID");
    			int tableID  = rs.getInt("TableId");
    			int billID  = rs.getInt("BillId");
    			Date reservationPlacedDate  = rs.getDate("reservationPlacedDate"); 
    			Time reservationPlacedTime  = rs.getTime("reservationPlacedTime");
    			boolean isNotified = rs.getInt("isNotified") == 1;

    			
    			//convert date and time to local
    			LocalDate resDate = reservationDate.toLocalDate();
    			LocalTime resTime = reservationTime.toLocalTime();
    			LocalDate placedDate = reservationPlacedDate.toLocalDate();
    			LocalTime placedTime = reservationPlacedTime.toLocalTime();
	        
    			r = new Reservation(reservationID,customerID,tableID,billID,numOfGuests,confirmation_code
    											,resDate,resTime,placedDate,placedTime,status, isNotified);
	        }
	        	        
		}
					
		 catch (SQLException e) {
			e.printStackTrace();
		}
		
		return r; 
	}
	
	
	/**
     * Identifies reservations assigned to a specific table that can no longer exist
     * due to table deletion or a reduction in seating capacity.
     * * @param tableId     The ID of the modified table.
     * @param newCapacity The new seat count (ignored if isDeletion is true).
     * @param isDeletion  True if the table is being removed entirely.
     * @return A List of Reservation objects that are now invalid.
     */
	public List<Reservation> getAffectedReservations(int tableId, int newCapacity, boolean isDeletion) {
		newCapacity = GetAllSeats();
	    List<Reservation> affected = new ArrayList<>();
	    int capacityToCheck = isDeletion ? newCapacity : 0;

	    // 1. First, FIND them to return the list for the popup
	    String selectQuery = "SELECT * FROM reservation WHERE status NOT IN ('CANCELLED', 'COMPLETED') " +
	                         "AND reservationDate >= CURDATE() AND numOfGuests > ?";

	    // 2. Second, UPDATE them to 'CANCELLED' in the database
	    String updateQuery = "UPDATE reservation SET status = 'CANCELLED', isNotified = 0 " +
	                         "WHERE status NOT IN ('CANCELLED', 'COMPLETED') " +
	                         "AND reservationDate >= CURDATE() AND numOfGuests > ?";

	    try (Connection con = getConnection()) {
	        con.setAutoCommit(false); // Use a transaction for safety

	        try (PreparedStatement selectPst = con.prepareStatement(selectQuery);
	             PreparedStatement updatePst = con.prepareStatement(updateQuery)) {

	            // Set params for Select
	            selectPst.setInt(1, capacityToCheck);
	            ResultSet rs = selectPst.executeQuery();
	            
	            while (rs.next()) {
	                affected.add(mapResultSetToReservation(rs));
	            }

	            // Set params for Update - THIS changes the database!
	            updatePst.setInt(1, capacityToCheck);
	            updatePst.executeUpdate();

	            con.commit(); // Save changes
	        } catch (SQLException e) {
	            con.rollback();
	            e.printStackTrace(); // או זריקת השגיאה הלאה
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return affected;
	}

	

	private void cancelReservation(Connection con, int resId) throws SQLException {
	    String sql = "UPDATE reservation SET status = 'CANCELLED', isNotified = 0, TableId = NULL WHERE reservationID = ?";
	    try (PreparedStatement pst = con.prepareStatement(sql)) {
	        pst.setInt(1, resId);
	        pst.executeUpdate();
	    }
	}
	
	/**
     * Helper method to map a single row from a ResultSet into a Reservation object.
     * * @param rs The ResultSet positioned at the desired row.
     * @return A fully populated Reservation entity.
     * @throws SQLException If database access error occurs.
     */
	private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
	    int reservationID = rs.getInt("reservationID");
	    Date reservationDate = rs.getDate("reservationDate"); 
	    Time reservationTime = rs.getTime("reservationTime");
	    int numOfGuests = rs.getInt("numOfGuests");
	    int confirmation_code = rs.getInt("confirmationCode");
	    
	    // Convert status string to Enum
	    enums.ReservationStatus status = enums.ReservationStatus.valueOf(rs.getString("status"));
	    
	    int customerID = rs.getInt("customerID");
	    int tableID = rs.getInt("TableId");
	    int billID = rs.getInt("BillId");
	    Date reservationPlacedDate = rs.getDate("reservationPlacedDate"); 
	    Time reservationPlacedTime = rs.getTime("reservationPlacedTime");
	    
	    // Boolean check: 1 is true, 0 is false
	    boolean isNotified = rs.getInt("isNotified") == 1;

	    // Conversion to Local types
	    LocalDate resDate = reservationDate.toLocalDate();
	    LocalTime resTime = reservationTime.toLocalTime();
	    LocalDate placedDate = (reservationPlacedDate != null) ? reservationPlacedDate.toLocalDate() : null;
	    LocalTime placedTime = (reservationPlacedTime != null) ? reservationPlacedTime.toLocalTime() : null;

	    return new Reservation(reservationID, customerID, tableID, billID, numOfGuests, confirmation_code,
	                           resDate, resTime, placedDate, placedTime, status, isNotified);
	}
	
	public int GetAllSeats() {
	    Connection con = getConnection();
	    int totalCapacity = 0;

	    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `table`")) {
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	            int tableId = rs.getInt("TableId");
	            int capacity = rs.getInt("Capacity");
	            Boolean isavailable = rs.getBoolean("IsAvailable");
	            totalCapacity += capacity;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    System.out.println("Total seats calculated: " + totalCapacity);
	    return totalCapacity;
	}
	
	
	
	/**
	 * Updates the reservation to mark that the user has seen the change notification.
	 * * @param resId The ID of the reservation
	 * @return true if successful
	 */
	public boolean markAsNotified(int resId) {
	    String sql = "UPDATE reservation SET isNotified = 1 WHERE reservationID = ?";
	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	        pst.setInt(1, resId);
	        return pst.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}