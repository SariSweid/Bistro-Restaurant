package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import DB.DBController;
import Entities.WaitingListEntry;

/**
 * DAO for managing the waiting list.
 */
public class WaitingListDAO extends DBController {

    /**
     * Remove an existing WaitngList from the database.
     *
     * @param w the WaitngList object containing updated data
     * @return true if the Remove succeeded, false otherwise
     */
	public Boolean removeFromWitingList(WaitingListEntry w) { 
		
		Connection con = getConnection(); //connect to DB
		
		try (PreparedStatement pst = con.prepareStatement("DELETE FROM `waitinglist` WHERE userID = ? ")){
			
			pst.setInt(1, w.getUserId()); // not exist yet
			
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
		public Boolean addToWitingList(WaitingListEntry w) { // not sure if i recive  Reservation Or User
			
			Connection con = getConnection();
			try (PreparedStatement pst = con.prepareStatement("INSERT INTO `waitinglist` (userID,contactInfo, numOfGuests, confirmationCode,entryTime,exitTime,exitReason) VALUES (?,?,?,?,?,?,?)")){
				
				pst.setInt(1, w.getUserId());
				pst.setString(2, w.getContactInfo() );
				pst.setInt(3, w.getNumOfGuests());
				pst.setInt(4, w.getConfirmationCode());
				pst.setTime(5, java.sql.Time.valueOf(w.getEntryTime()));
				pst.setTime(6, java.sql.Time.valueOf(w.getExitTime()));					
				pst.setString(7, w.getExitReason().name());
				
			
	            
	            int update_status = pst.executeUpdate();
	            return update_status > 0;
	
		        } catch (SQLException e) {
		        	e.printStackTrace();
		        	return false;
		        }
				
			}
}
