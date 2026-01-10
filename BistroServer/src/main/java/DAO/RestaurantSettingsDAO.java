package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import DB.DBController;
import Entities.RestaurantSettings;
import Entities.WeeklyOpeningHours;
import enums.Day;

/**
 * DAO for managing restaurant settings.
 */
public class RestaurantSettingsDAO extends DBController {
	
    public List<WeeklyOpeningHours> getAllWeeklyOpeningHours() {
    	Connection con = getConnection();
    	List<WeeklyOpeningHours> hoursList = new ArrayList<>();
    	try (PreparedStatement pst = con.prepareStatement("SELECT * FROM restaurantsettings")){
    		ResultSet rs = pst.executeQuery();
    	
            while (rs.next()) {
                Day day = Day.valueOf(rs.getString("Day"));
                LocalTime openingTime = LocalTime.parse(rs.getString("OpeningHours"));
                LocalTime closingTime = LocalTime.parse(rs.getString("ClosingHours"));
                
                WeeklyOpeningHours hours = new WeeklyOpeningHours(openingTime, closingTime , day);
                hoursList.add(hours);
    	}
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hoursList;
    }
    
    /**
     * Updates the OpeningHours an existing restaurantsettings in the database.
     *
     * @param r the SpecialDates object containing updated data
     * @param d the Day we want to change
     * @return true if the update succeeded, false otherwise
     */
	public Boolean updateOpeningHours(RestaurantSettings r , Day d  ) {
		
		Connection con = getConnection(); //connect to DB
		try (PreparedStatement pst = con.prepareStatement("UPDATE `restaurantsettings` SET OpeningHours = ? WHERE Day = ? ")){
			
			
			WeeklyOpeningHours wh = r.getOpeningHoursForDay(d);
			
			pst.setTime(1, java.sql.Time.valueOf(wh.getOpeningTime()));
			pst.setString(2, d.name()); 
		
            int rows = pst.executeUpdate();

            return rows > 0;
	        } catch (SQLException e) {
	        	System.err.println("SQL Exception during update: " + e.getMessage());
	        	e.printStackTrace();
	        	return false; 
        }			
	}

    /**
     * Updates the ClosingHours an existing restaurantsettings in the database.
     *
     * @param r the SpecialDates object containing updated data
     * @param d the Day we want to change
     * @return true if the update succeeded, false otherwise
     */
	public Boolean updateClosingHours(RestaurantSettings r , Day d) {
		
		Connection con = getConnection(); //connect to DB
		try (PreparedStatement pst = con.prepareStatement("UPDATE `restaurantsettings` SET ClosingHours = ? WHERE Day = ? ")){
			
			WeeklyOpeningHours wh = r.getOpeningHoursForDay(d);
			
			pst.setTime(1, java.sql.Time.valueOf(wh.getClosingTime()));
			pst.setString(2, d.name()); 
			
            int rows = pst.executeUpdate();

            return rows > 0;
	        } catch (SQLException e) {
        	System.err.println("SQL Exception during update: " + e.getMessage());
        	e.printStackTrace();
        	return false; 
        }	
		
	}
	
	
	
    /**
     * Updates the MaxTable an existing restaurantsettings in the database.
     *
     * @param r the RestaurantSettings object containing updated data
     * @return true if the update succeeded, false otherwise
     */
	public Boolean updateMaxTable(RestaurantSettings r) {
		
		Connection con = getConnection(); //connect to DB
		try (PreparedStatement pst = con.prepareStatement("UPDATE `restaurantsettings` SET MaxTables = ? ")){
			
			
			
			pst.setInt(1, r.getMaxTables()); 
			
            int rows = pst.executeUpdate();

            return rows > 0;
	        } catch (SQLException e) {
        	System.err.println("SQL Exception during update: " + e.getMessage());
        	e.printStackTrace();
        	return false; 
        }	
		
	}


}
