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
 * Data Access Object (DAO) for managing general restaurant configurations.
 * This class handles the persistence of WeeklyOpeningHours and global 
 * settings like the maximum table capacity allowed in the restaurant.
 */
public class RestaurantSettingsDAO extends DBController {

    /**
     * Retrieves the standard weekly schedule of the restaurant.
     *
     * @return a List of WeeklyOpeningHours representing 
     * the opening and closing times for each day of the week.
     */
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
     * Inserts a new daily schedule record into the settings.
     * Defaults the MaxTables to 10 for new entries.
     *
     * @param hours the schedule to insert.
     * @return true if successful.
     */
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

    /**
     * Updates only the opening time for a specific day using the main settings object.
     * * @param r the RestaurantSettings object.
     * @param d the Day to update.
     * @return true if updated successfully.
     */
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

    /**
     * Updates only the closing time for a specific day using the main settings object.
     * * @param r the RestaurantSettings object.
     * @param d the Day to update.
     * @return true if updated successfully.
     */
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
     * Removes the schedule for a specific day from the database.
     *
     * @param day the Day enum to remove.
     * @return true if deletion succeeded.
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

    /**
     * Updates the global maximum number of tables allowed in the restaurant.
     * This limit affects the availability logic for all future reservations.
     *
     * @param r the RestaurantSettings object containing the new max limit.
     * @return true if successful.
     */
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
     * Checks if a configuration for a specific day already exists in the database.
     * * @param day the Day to check.
     * @return true if a record exists.
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

	/**
	 * Permanently removes a special date entry from the database.
	 * * @param date the LocalDate to remove.
	 * @return true if the deletion succeeded.
	 */
	public boolean deleteSpecialDate(LocalDate date) {
		Connection con = getConnection();
		try(PreparedStatement pst = con.prepareStatement("DELETE FROM `specialdates` WHERE special_date = ?")){
			
		pst.setDate(1,java.sql.Date.valueOf(date));
		int rows = pst.executeUpdate();
        return rows > 0;
				
		}catch(SQLException e) { 
            System.err.println("SQL Exception during delete: " + e.getMessage());
            e.printStackTrace();
            return false; 
		}
	}

    /**
     * Updates the opening hours based on a WeeklyOpeningHours object.
     * * @param hours the object containing the day and new time.
     * @return true if successful.
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
     * Updates the closing hours based on a WeeklyOpeningHours object.
     * * @param hours the object containing the day and new time.
     * @return true if successful.
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