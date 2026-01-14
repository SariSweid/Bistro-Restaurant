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
import enums.ReservationStatus;

/**
 * DAO for handling Tables in the restaurant.
 */
public class TableDAO extends DBController {

    /**
     * Updates the availability status of a table.
     *
     * @param tableId the ID of the table to update
     * @param isAvailable true if the table should be free, false if occupied
     * @return true if the update succeeded, false otherwise
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
     * Retrieves a specific table by their ID.
     *
     * @param TableId the table ID
     * @return the Table if found, or null if not found
     */
	public Table GetTable(int tableid) {
		
		Connection con = getConnection(); //connect to DB
		Table t = null;
		
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `table` WHERE TableId = ?")){
			pst.setInt(1, tableid);
			ResultSet rs = pst.executeQuery();
			
	        if (rs.next()) {
	        	int tableId = rs.getInt("TableId");
	        	int capacity = rs.getInt("Capacity");
	        	Boolean isAvailable = rs.getBoolean("IsAvailable");	        	
	        	//wait for table class
	        	t = new Table(tableId , capacity , isAvailable ); // not exist yet
		        }
    	        
		}
			
	catch (SQLException e) {
		e.printStackTrace();
	}

	return t; // There isnt Res with this ID.
	}
	

			
	

	
	
    /**
     * Retrieves all Tables
     *
     * @return a list of Tables
     */
	public List<Table> GetAllTables(){
		
		Connection con = getConnection(); //connect to DB
    	List<Table> tables = new ArrayList<>(); // made new list to return
    	
    	try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `table`")){ // ask from DB the all Orders
    		
    		ResultSet rs = pst.executeQuery();
    		
    		while(rs.next()) { // read from DB
			
    			int tableId  = rs.getInt("TableId");
    			int capacity  = rs.getInt("Capacity"); 
    			Boolean isavailable = rs.getBoolean("IsAvailable");
    			//wait for table class
    			Table t = new Table(tableId , capacity , isavailable ); // not exist yet
    			tables.add(t);
    		
    	}
    		
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	 return tables;		
	}

	
	
	
	
	
    /**
     * Updates an existing table in the database.
     *
     * @param t the table object containing updated data
     * @return true if the update succeeded, false otherwise
     */
	public Boolean UpdateTable(Table t) {
		
		Connection con = getConnection(); //connect to DB
		
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
     * Remove an existing table from the database.
     *
     * @param t the table object containing updated data
     * @return true if the Remove succeeded, false otherwise
     */
	public Boolean DeleteTable(Table t) {
		

		Connection con = getConnection(); //connect to DB
		
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
	 * Finds an available table for a given number of guests at a specific date and time.
	 * Considers overlapping reservations and table availability.
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
	        "          AND ABS(TIMESTAMPDIFF(MINUTE, r.reservationTime, ?)) < 120 " +  // 2-hour duration
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
	 * Frees the table assigned to a reservation and updates the reservation status to COMPLETED.
	 *
	 * @param reservationId the ID of the reservation whose table should be freed
	 * @return true if the table and reservation were successfully updated, false otherwise
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
	 * Retrieves a reservation by its ID.
	 *
	 * @param ReservationId the reservation ID
	 * @return the Reservation if found, or null if not found
	 */
	public Reservation GetReservation(int ReservationId) {
		
		Connection con = getConnection(); //connect to DB
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

    			
    			//conver date and time to local
    			LocalDate resDate = reservationDate.toLocalDate();
    			LocalTime resTime = reservationTime.toLocalTime();
    			LocalDate placedDate = reservationPlacedDate.toLocalDate();
    			LocalTime placedTime = reservationPlacedTime.toLocalTime();
	        

	            
	            
    			r = new Reservation(reservationID,customerID,tableID,billID,numOfGuests,confirmation_code
    											,resDate,resTime,placedDate,placedTime,status);
	        }
	        	        
		}
					
		 catch (SQLException e) {
			e.printStackTrace();
		}
		
		return r; // There isnt Res with this ID.
	}


}
