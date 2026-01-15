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
    
    

    public boolean insertWeeklyOpeningHours(WeeklyOpeningHours hours) {
        Connection con = getConnection();
        try (PreparedStatement pst = con.prepareStatement(
                "INSERT INTO restaurantsettings (Day, MaxTables, OpeningHours, ClosingHours) VALUES (?, ?, ?, ?)")) {
            pst.setString(1, hours.getDay().name());
            pst.setInt(2, 10);
            pst.setTime(3, java.sql.Time.valueOf(hours.getOpeningTime()));
            pst.setTime(4, java.sql.Time.valueOf(hours.getClosingTime()));
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean updateOpeningHours(RestaurantSettings r, Day d) {
        Connection con = getConnection();
        try (PreparedStatement pst = con.prepareStatement("UPDATE `restaurantsettings` SET OpeningHours = ? WHERE Day = ?")) {
            WeeklyOpeningHours wh = r.getOpeningHoursForDay(d);
            pst.setTime(1, java.sql.Time.valueOf(wh.getOpeningTime()));
            pst.setString(2, d.name());
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean updateClosingHours(RestaurantSettings r, Day d) {
        Connection con = getConnection();
        try (PreparedStatement pst = con.prepareStatement("UPDATE `restaurantsettings` SET ClosingHours = ? WHERE Day = ?")) {
            WeeklyOpeningHours wh = r.getOpeningHoursForDay(d);
            pst.setTime(1, java.sql.Time.valueOf(wh.getClosingTime()));
            pst.setString(2, d.name());
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * Deletes a specific day's weekly opening hours from the database.
     *
     * @param day the day to delete
     * @return true if deletion succeeded, false otherwise
     */
	    public boolean deleteWeeklyOpeningHours(Day day) {
	        Connection con = getConnection();
	        try (PreparedStatement pst = con.prepareStatement(
	                "DELETE FROM restaurantsettings WHERE Day = ?")) {
	            pst.setString(1, day.name());
	            int rows = pst.executeUpdate();
	            return rows > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

    public Boolean updateMaxTable(RestaurantSettings r) {
        Connection con = getConnection();
        try (PreparedStatement pst = con.prepareStatement("UPDATE `restaurantsettings` SET MaxTables = ?")) {
            pst.setInt(1, r.getMaxTables());
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a given day already exists in the database.
     *
     * @param day the Day to check
     * @return true if the day exists in the DB, false otherwise
     */
    public boolean existsDay(Day day) {
        Connection con = getConnection();
        try (PreparedStatement pst = con.prepareStatement("SELECT 1 FROM restaurantsettings WHERE Day = ?")) {
            pst.setString(1, day.name());
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


            
	
	//deletes a special date from the db
	public boolean deleteSpecialDate(LocalDate date) {
		Connection con = getConnection();
		try(PreparedStatement pst = con.prepareStatement("DELETE FROM 'specialdates")){
			
		pst.setDate(1,java.sql.Date.valueOf(date));

		int rows = pst.executeUpdate();

        return rows > 0;
				
					
		}catch(SQLException e) { 
    	System.err.println("SQL Exception during update: " + e.getMessage());
    	e.printStackTrace();
    	return false; 
		}
	
	}
	
	

    /**
     * Updates the opening hours for an existing day in the database.
     *
     * @param hours WeeklyOpeningHours object containing the updated opening time and day
     * @return true if the update succeeded, false otherwise
     */
    public boolean updateOpeningHours(WeeklyOpeningHours hours) {
        Connection con = getConnection();
        try (PreparedStatement pst = con.prepareStatement(
                "UPDATE restaurantsettings SET OpeningHours = ? WHERE Day = ?")) {
            pst.setTime(1, java.sql.Time.valueOf(hours.getOpeningTime()));
            pst.setString(2, hours.getDay().name());
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the closing hours for an existing day in the database.
     *
     * @param hours WeeklyOpeningHours object containing the updated closing time and day
     * @return true if the update succeeded, false otherwise
     */
    public boolean updateClosingHours(WeeklyOpeningHours hours) {
        Connection con = getConnection();
        try (PreparedStatement pst = con.prepareStatement(
                "UPDATE restaurantsettings SET ClosingHours = ? WHERE Day = ?")) {
            pst.setTime(1, java.sql.Time.valueOf(hours.getClosingTime()));
            pst.setString(2, hours.getDay().name());
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
