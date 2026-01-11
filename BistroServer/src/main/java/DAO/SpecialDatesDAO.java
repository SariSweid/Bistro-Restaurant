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
import Entities.SpecialDates;

/**
 * DAO for Special Dates in the restaurant.
 */
public class SpecialDatesDAO extends DBController {

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
     * Updates an existing SpecialDates in the database.
     *
     * @param d the SpecialDates object containing updated data
     * @return true if the update succeeded, false otherwise
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
     * Inserts a new SpecailDates into the database.
     *
     * @param d the SpecailDates to insert
     * @return true if the insertion succeeded, false otherwise
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
	
	
	public boolean deleteSpecialDate(LocalDate date) {
	    Connection con = getConnection();

	    try (PreparedStatement pst =
	            con.prepareStatement("DELETE FROM restaurant_main.specialdates WHERE special_date = ?\r\n"
	            		+ "")) {

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
