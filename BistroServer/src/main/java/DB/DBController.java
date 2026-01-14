	package DB;
	
	import java.sql.Connection;
	import java.sql.Date;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Time;
	import java.sql.Timestamp;
	import java.time.LocalDate;
	import java.time.LocalDateTime;
	import java.time.LocalTime;
	import java.util.ArrayList;
	import java.util.List;
	
	import Entities.Bill;
	import Entities.Guest;
	import Entities.Manager;
	import Entities.Report;
	import Entities.Reservation;
	import Entities.RestaurantSettings;
	import Entities.SpecialDates;
	import Entities.Table;
import Entities.TimeData;
import Entities.User;
	import Entities.WaitingListEntry;
	import Entities.WeeklyOpeningHours;
	import enums.Day;
import enums.ReservationStatus;
import logicControllers.UserFactory;
	import Entities.Subscriber;
	import Entities.Supervisor;
	
	
	
	/**
	 * DBController is responsible for managing database operations
	 * related to reservations, including CRUD operations.
	 */
	public class DBController {
		
		
	
	
		    private static Connection conn = null; 
		    private static long lastUsed = 0;
		    private static final long TIMEOUT = 30 * 60 * 1000; // 30 min 
	
		    /**
		     * Returns a valid connection to the database.
		     * If the connection was inactive for more than 30 minutes,
		     * it is closed and reinitialized.
		     *
		     * @return an active SQL Connection
		     */
		    public static Connection getConnection() {
		        try {
		            if (conn != null && !conn.isClosed()) {
		                if (System.currentTimeMillis() - lastUsed > TIMEOUT) {
		                    conn.close();
		                    conn = null;
		                    System.out.println("SQL connection closed due to inactivity");
		                }
		            }
		    	
		    	
		    	
		            if (conn == null || conn.isClosed()) {	                
		              conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_main?serverTimezone=Asia/Jerusalem&useSSL=false","root","sare1020"); // sari -DB
	
		              //conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_main?serverTimezone=Asia/Jerusalem&useSSL=false","root","Root1234"); //leon -db
		              //conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_main?serverTimezone=Asia/Jerusalem&useSSL=false","root","Vrek2logos@"); //tamer
		                System.out.println("SQL connection initialized");	               	                	                
		            }
		            lastUsed = System.currentTimeMillis();
		        }
		            
		        catch (SQLException e) {
		        	e.printStackTrace();
		        }	        
		        return conn;
		    }
	}	    	
	
	
	
	
	
	
