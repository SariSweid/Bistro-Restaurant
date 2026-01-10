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
        Connection con = getConnection();
        try (PreparedStatement pst = con.prepareStatement(
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
	 * Assigns a waiting customer to an available table if possible.
	 *
	 * @return true if a waiting customer was successfully assigned, false otherwise
	 */
	public Boolean notifyTableIsAvailable() throws SQLException {
	    Connection con = getConnection();
	    

	    try {
	       PreparedStatement pst1 = con.prepareStatement("SELECT userID, numOfGuests FROM waitinglist LIMIT 1"); // first we  check if there is waitings
	        ResultSet rs = pst1.executeQuery();

	        if (!rs.next()) 
	            return false; 
	        
	        int userID = rs.getInt("userID");
	        int numGuests = rs.getInt("numOfGuests");
	        


	        PreparedStatement pst2 = con.prepareStatement("SELECT TableId FROM `table`  WHERE IsAvailable=1 AND capacity >= ? ORDER BY capacity LIMIT 1"); // than we search for him a table
	        pst2.setInt(1, numGuests);

	        ResultSet rs2 = pst2.executeQuery();
	        if (!rs2.next()) 
	           return false; 
	        

	        int tableId = rs2.getInt("TableId");

	        PreparedStatement pst3 = con.prepareStatement("DELETE FROM waitinglist WHERE userID=?"); // we remove him from the waiting list
	        pst3.setInt(1, userID);
	        int deleted = pst3.executeUpdate();

	        
	        PreparedStatement pst4 = con.prepareStatement("UPDATE `table` SET IsAvailable=0 WHERE TableId=?"); // update the table that he is taken
	        pst4.setInt(1, tableId);
	        int updated = pst4.executeUpdate();
	        
	        return deleted > 0 && updated > 0;

	        

	    } catch (Exception e) {
        	System.err.println("SQL Exception during update: " + e.getMessage());
        	e.printStackTrace();
        	return false;
	    }
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
