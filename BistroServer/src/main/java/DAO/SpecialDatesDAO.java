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
import Entities.SpecialDates;

/**
 * Data Access Object (DAO) for managing SpecialDates in the restaurant.
 * This class handles special operating hours, holiday closures, and event-specific 
 * scheduling that overrides the standard restaurant timetable.
 */
public class SpecialDatesDAO extends DBController {

    /**
     * Retrieves a full list of all special dates and their associated operating hours.
     * Useful for checking availability during reservation processes or for admin management.
     *
     * @return a List of SpecialDates objects.
     */
    public List<SpecialDates> getAllSpecialDates() {
    	Connection con = getConnection();
    	List<SpecialDates> specialDatesList  = new ArrayList<>();
    	try (PreparedStatement pst = con.prepareStatement("SELECT * FROM specialdates")){
    		ResultSet rs = pst.executeQuery();
    	
            while (rs.next()) {
                LocalDate date = rs.getDate("special_date").toLocalDate();
                LocalTime openingTime = rs.getTime("OpeningHours").toLocalTime();
                LocalTime closingTime = rs.getTime("ClosingHours").toLocalTime();
                String description = rs.getString("description");
                
                SpecialDates hours = new SpecialDates(openingTime, closingTime  , date ,  description);
                specialDatesList .add(hours);
    	}
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return specialDatesList ;
    }
    
    /**
     * Updates an existing record for a special date in the database.
     * This is used when a planned event's hours or description change.
     *
     * @param oldDate the original date to be used in the WHERE clause.
     * @param d       the SpecialDates object containing the new data.
     * @return true if the update was successful, false otherwise.
     */
	public Boolean updateSpecialDates(LocalDate oldDate, SpecialDates d) {
		
		Connection con = getConnection(); //connect to DB
		
		try (PreparedStatement pst = con.prepareStatement("UPDATE `specialdates`"+" SET special_date = ? , OpeningHours=?, ClosingHours = ? , description = ?"+"   WHERE special_date = ? ")){
			
			pst.setDate(1, java.sql.Date.valueOf(d.getDate()));
			pst.setTime(2, java.sql.Time.valueOf(d.getOpeningTime()));
			pst.setTime(3, java.sql.Time.valueOf(d.getClosingTime()));
			pst.setString(4, d.getDescription());
			pst.setDate(5,java.sql.Date.valueOf(oldDate));
			
            int rows = pst.executeUpdate();

            return rows > 0;
	        } catch (SQLException e) {
        	System.err.println("SQL Exception during update: " + e.getMessage());
        	e.printStackTrace();
        	return false; 
        }			
	}
	
    /**
     * Inserts a new special date configuration into the database.
     * Use this to define holidays or special events with custom opening and closing times.
     *
     * @param d the SpecialDates object to insert.
     * @return true if the insertion was successful.
     */
	public Boolean addSpecialDates(SpecialDates d) {
		Connection con = getConnection(); //connect to DB
		
		try (PreparedStatement pst = con.prepareStatement("INSERT INTO `specialdates` (special_date, OpeningHours, ClosingHours, description) VALUES (?,?,?,?)")){  
			
			pst.setDate(1, java.sql.Date.valueOf(d.getDate()));
			pst.setTime(2, java.sql.Time.valueOf(d.getOpeningTime()));
			pst.setTime(3, java.sql.Time.valueOf(d.getClosingTime()));
			pst.setString(4, d.getDescription());
            
            int update_status = pst.executeUpdate();
            return update_status > 0;

	        } catch (SQLException e) {
	        	e.printStackTrace();
	        	return false;
	        }
		}
	
    /**
     * Permanently removes a special date entry from the database.
     * Once deleted, the restaurant will revert to its standard operating hours for this date.
     *
     * @param date the specific date to remove.
     * @return true if the deletion succeeded.
     */
	public boolean deleteSpecialDate(LocalDate date) {
	    Connection con = getConnection();

	    try (PreparedStatement pst =
	            con.prepareStatement("DELETE FROM restaurant_main.specialdates WHERE special_date = ?")) {

	        pst.setDate(1, java.sql.Date.valueOf(date));
	        int rows = pst.executeUpdate();
	        return rows > 0;

	    } catch (SQLException e) {
	    	System.err.println("SQL Exception during delete: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
}