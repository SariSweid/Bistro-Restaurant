package DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import Entities.Bill;
import Entities.Guest;
import Entities.Report;
import Entities.Reservation;
import Entities.RestaurantManager;
import Entities.RestaurantSettings;
import Entities.RestaurantSupervisor;
import Entities.SpecialDates;
import Entities.Table;
import Entities.User;
import Entities.WaitingListEntry;
import Entities.WeeklyOpeningHours;
import enums.Day;
import logicControllers.UserFactory;
import Entities.Subscriber;



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
	                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_main?serverTimezone=Asia/Jerusalem&useSSL=false","root","Root1234");
	                System.out.println("SQL connection initialized");	               	                	                
	            }
	            lastUsed = System.currentTimeMillis();
	        }
	            
	        catch (SQLException e) {
	        	e.printStackTrace();
	        }	        
	        return conn;
	    }
	    
	    
	    
	    /**
	     * Updates an existing reservation in the database.
	     *
	     * @param reservationId the reservation ID to update
	     * @param newDate the new reservation date
	     * @param newNumGuests the updated number of guests
	     * @return true if the update succeeded, false otherwise
	     */
	    
	    public boolean updateReservation(Reservation reservation) {
	        Connection con = getConnection();
;

	        try (PreparedStatement pst = con.prepareStatement("UPDATE `reservation` SET reservationDate = ?, reservationTime = ?, numOfGuests = ?,  status = ?  WHERE reservationID = ?")) {


	        	pst.setDate(1, Date.valueOf(reservation.getReservationDate()));
	        	pst.setTime(2, Time.valueOf(reservation.getReservationTime()));  
	            pst.setInt(3, reservation.getNumOfGuests());
	            pst.setString(4, reservation.getStatus().name());  

	            pst.setInt(5, reservation.getReservationID());

	            int rows = pst.executeUpdate();

	            return rows > 0; 

	        } catch (SQLException e) {
	            System.err.println("SQL Exception during update: " + e.getMessage());
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


		/**
	     * “Which tables are already taken at THIS exact time?”
	     *
	     * @return a list reservations at the specific date (input)
	     */
		// ADDED:
		public List<Reservation> getReservationsAt(LocalDate date, LocalTime time) {
		    List<Reservation> list = new ArrayList<>();
		    Connection con = getConnection();

		    try (PreparedStatement pst = con.prepareStatement(
		        "SELECT * FROM `reservation` " +
		        "WHERE reservationDate = ? AND reservationTime = ? " +
		        "AND status IN ('CONFIRMED','PENDING','SEATED')"
		    )) {
		        pst.setDate(1, Date.valueOf(date));
		        pst.setTime(2, Time.valueOf(time));

		        ResultSet rs = pst.executeQuery();
		        while (rs.next()) {
		            list.add(new Reservation(
		                rs.getInt("reservationID"),
		                rs.getInt("customerID"),
		                rs.getInt("TableId"),
		                rs.getInt("BillId"),
		                rs.getInt("numOfGuests"),
		                rs.getInt("confirmationCode"),
		                rs.getDate("reservationDate").toLocalDate(),
		                rs.getTime("reservationTime").toLocalTime(),
		                rs.getDate("reservationPlacedDate").toLocalDate(),
		                rs.getTime("reservationPlacedTime").toLocalTime(),
		                enums.ReservationStatus.valueOf(rs.getString("status"))
		            ));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return list;
		}

	    
	    /**
	     * Retrieves all reservations from the database.
	     *
	     * @return a list of all reservations
	     */
	    public List<Reservation> readAllReservations() {
	    	
	    	List<Reservation> reservations = new ArrayList<>(); // made new list to return
	    	Connection con = getConnection(); //connect to DB
	    	
	    	try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `reservation`")){ // ask from DB the all Orders 
	    		
	    		ResultSet rs = pst.executeQuery();
	    		
	    		while(rs.next()) { // read from DB
	    			
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
	    			
	    			Reservation r = new Reservation(reservationID,customerID,tableID,billID,numOfGuests,confirmation_code
	    											,resDate,resTime,placedDate,placedTime,status);
	    			
	                reservations.add(r);
	    			
	    		}
	    		
	    		for (Reservation r : reservations) {
	    		    System.out.println(r);
	    		}
	    		
	    		
	    	} catch (SQLException e) {

				e.printStackTrace();
			}
	    	
	    	
			return reservations;    	
	    }
	    	    
  
	    
	    /**
	     * Inserts a new reservation into the database.
	     * The database auto-generates reservationID.
	     * Updates the reservation object with the assigned ID.
	     *
	     * @param r the reservation to insert
	     * @return true if insertion succeeded, false otherwise
	     */
	    public boolean insertReservation(Reservation r) {
	        Connection con = getConnection();

	        try (PreparedStatement pst = con.prepareStatement(
	                "INSERT INTO `reservation` " + 
	                "(customerID, tableId, billId, numOfGuests, confirmationCode, reservationDate, reservationTime, reservationPlacedDate, reservationPlacedTime, status) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
	                java.sql.Statement.RETURN_GENERATED_KEYS // Allows us to retrieve auto-generated reservationID
	        )) {

	        	
	            pst.setInt(1, r.getCustomerId());
	            pst.setObject(2, r.getTableID(), java.sql.Types.INTEGER);
	            pst.setObject(3, r.getBillID(), java.sql.Types.INTEGER);
	            pst.setInt(4, r.getNumOfGuests());
	            pst.setInt(5, r.getConfirmationCode());
	            pst.setDate(6, java.sql.Date.valueOf(r.getReservationDate()));
	            pst.setTime(7, java.sql.Time.valueOf(r.getReservationTime()));
	            pst.setDate(8, java.sql.Date.valueOf(r.getReservationPlacedDate()));
	            pst.setTime(9, java.sql.Time.valueOf(r.getReservationPlacedTime()));
	            pst.setString(10, r.getStatus().name());

	            int update_status = pst.executeUpdate();

	            // Retrieve the auto-generated reservationID
	            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    r.setReservationID(generatedKeys.getInt(1));
	                }
	            }

	            return update_status > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

		
	    
	    
	    /**
	     * Inserts a new user into the database.
	     *
	     * @param u the user to insert
	     * @return true if the insertion succeeded, false otherwise
	     */
		public Boolean InsertUser(User u) {
			
			Connection con = getConnection();
			
	        try (PreparedStatement pst = con.prepareStatement(
		            "INSERT INTO `user`  (UserId, Name, Phone, Email, UserName, MemberShipCode, Role)  VALUES (?, ?, ?, ?, ?, ?, ?)"
		        )) {
	        	
	            pst.setInt(1, u.getUserId()); 
	            pst.setString(2, null);
	            pst.setString(3, u.getPhone());
	            pst.setString(4, u.getEmail());
	            pst.setString(5, null);
	            pst.setInt(6, 0);
	            pst.setString(7, u.getRole().name());
	            
	            if (u instanceof Subscriber s) {
	                pst.setString(2, s.getName());
	                pst.setString(5, s.getUserName());
	                pst.setInt(6, s.getMembershipCode());
	            }
	            
	            else if (u instanceof RestaurantManager m) {
	                pst.setString(2, m.getName());
	                pst.setString(5, m.getUserName());
	            }
	            else if (u instanceof RestaurantSupervisor s) {
	                pst.setString(2, s.getName());
	                pst.setString(5, s.getUserName());
	            }

	        	        
	            int update_status = pst.executeUpdate();
	            return update_status > 0;

	        } catch (SQLException e) {
	        	e.printStackTrace();
	        	return false;
	        }
			
		}
		
		
		
		
		
		
	    /**
	     * Updates an existing user in the database.
	     *
	     * @param u the user object containing updated data
	     * @return true if the update succeeded, false otherwise
	     */
		public Boolean UpdateUser(User u) {
			Connection con = getConnection();
			
			try (PreparedStatement pst = con.prepareStatement("UPDATE `user` SET Name = ?, Phone = ?, Email = ?, UserName = ?  WHERE UserId = ?")) {	
				
				pst.setString(1, null);
				pst.setString(2, u.getPhone());  
	            pst.setString(3, u.getEmail());
	            pst.setString(4, null);
  	            pst.setInt(5, u.getUserId());
  	            
  	            
	            if (u instanceof Subscriber s) {
	                pst.setString(1, s.getName());
	                pst.setString(4, s.getUserName());
	            }
	            	            
	            else if (u instanceof RestaurantManager m) {
	                pst.setString(1, m.getName());
	                pst.setString(4, m.getUserName());
	            }
	            else if (u instanceof RestaurantSupervisor s) {
	                pst.setString(1, s.getName());
	                pst.setString(4, s.getUserName());
	            }
	            int rows = pst.executeUpdate();

	            return rows > 0; 

	        } catch (SQLException e) {
	            System.err.println("SQL Exception during update: " + e.getMessage());
	            e.printStackTrace();
	            return false; 
	        }			
		}
		
		
		
		
	    /**
	     * Reads all users from the database.
	     *
	     * @return list of users
	     */
		public List<User> readAllUsers() {

		    List<User> users = new ArrayList<>();
		    Connection con = getConnection();

		    try (PreparedStatement pst =
		            con.prepareStatement("SELECT * FROM `user`")) {

		        ResultSet rs = pst.executeQuery();

		        while (rs.next()) {
		            User u = UserFactory.createUser(rs); 
		            users.add(u);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return users;
		}

	    
	    
	    /**
	     * Retrieves a specific user by their ID.
	     *
	     * @param userId the user ID
	     * @return the User if found, or null if not found
	     */
	    public User getUser(int userId) {

	        Connection con = getConnection();
	        User u = null;

	        try (PreparedStatement pst =
	                con.prepareStatement("SELECT * FROM `user` WHERE UserId = ?")) {

	            pst.setInt(1, userId);
	            ResultSet rs = pst.executeQuery();

	            if (rs.next()) {
	                u = UserFactory.createUser(rs); 
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return u;
	    }

			
	    // ---- ADDED - get User code----
	    // Fetch subscriber by membership code
	    public User getUserByMembershipCode(int code) {
	        try (Connection con = getConnection();
	             PreparedStatement pst = con.prepareStatement(
	                 "SELECT * FROM `user` WHERE MemberShipCode = ?")) {
	            pst.setInt(1, code);
	            ResultSet rs = pst.executeQuery();
	            if (rs.next()) return UserFactory.createUser(rs);
	        } catch (SQLException e) { e.printStackTrace(); }
	        return null;
	    }
		

	    /**
	     * Retrieves all reservations made by a specific user.
	     *
	     * @param user the user whose reservations are to be retrieved
	     * @return a list of reservations made by the user
	     */
		public List<Reservation> GetAllUserReservations(User user) {

		    Connection con = getConnection();
		    List<Reservation> reservations = new ArrayList<>();

		    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `reservation` WHERE customerID = ?")) {

		        pst.setInt(1, user.getUserId());
		        ResultSet rs = pst.executeQuery();

		        while (rs.next()) {

		            int reservationID = rs.getInt("reservationID");
		            Date reservationDate = rs.getDate("reservationDate");
		            Time reservationTime = rs.getTime("reservationTime");
		            int numOfGuests = rs.getInt("numOfGuests");
		            int confirmationCode = rs.getInt("confirmationCode");
		            enums.ReservationStatus status = enums.ReservationStatus.valueOf(rs.getString("status"));
		            int customerID = rs.getInt("customerID");
		            int tableID = rs.getInt("TableId");
		            int billID = rs.getInt("BillId");
		            Date placedDate = rs.getDate("reservationPlacedDate");
		            Time placedTime = rs.getTime("reservationPlacedTime");

		            Reservation r = new Reservation(reservationID, customerID, tableID, billID, numOfGuests, confirmationCode, reservationDate.toLocalDate(), reservationTime.toLocalTime(), placedDate.toLocalDate(), placedTime.toLocalTime(), status);


		            reservations.add(r);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return reservations;
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
	     * Inserts a new report into the database.
	     *
	     * @param r the report to insert
	     * @return true if the insertion succeeded, false otherwise
	     */
		public Boolean AddReport(Report r) { // 
			
			Connection con = getConnection(); //connect to DB
			
			try (PreparedStatement pst = con.prepareStatement("INSERT INTO `report` (Report_Id, Type, `From`, `To`, generatedAt,content) VALUES (?,?,?,?,?,?)")){
				
	            pst.setInt(1, r.getReportID()); 
	            pst.setString(2, r.getReportType().name()); 
	            pst.setDate(3, java.sql.Date.valueOf(r.getStartDate())); 
	            pst.setDate(4, java.sql.Date.valueOf(r.getEndDate()));
	            pst.setTimestamp(5, java.sql.Timestamp.valueOf(r.getGeneratedAt()));
	            pst.setString(6, r.getContent());
	            
	            int update_status = pst.executeUpdate();
	            return update_status > 0;
	
		        } catch (SQLException e) {
		        	e.printStackTrace();
		        	return false;
		        }			
			}
		
		

		
	    /**
	     * Retrieves all Reports
	     *
	     * @return a list of Reports
	     */
		public List<Report> GetAllReports() {

			Connection con = getConnection(); //connect to DB			
			List<Report> reports = new ArrayList<>(); // made new list to return
			
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `report`")){ // ask from DB the all reports
				
				ResultSet rs = pst.executeQuery();
				
	    		while(rs.next()) { // read from DB
    			
	    			int tableId  = rs.getInt("Report_Id");
	    			enums.ReportType Type  = enums.ReportType.valueOf(rs.getString("Type")); 
	    			LocalDate from = rs.getDate("From").toLocalDate(); 
	    			LocalDate to = rs.getDate("To").toLocalDate();
	    			LocalDateTime generatedAt = rs.getTimestamp("generatedAt").toLocalDateTime();
	    			String contant = rs.getString("content");
	    			
	    			
	    			
	    			
	    			Report r = new Report(tableId , Type , from , to ,generatedAt,contant); // not exist yet
	    			reports.add(r);
	    		
	    		}			
			} catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	 return reports;		
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
	     * @param r the SpecialDates object containing updated data
	     * @param d the Day we want to change
	     * @return true if the update succeeded, false otherwise
	     */
		public Boolean updateMaxTable(RestaurantSettings r , Day d) {
			
			Connection con = getConnection(); //connect to DB
			try (PreparedStatement pst = con.prepareStatement("UPDATE `restaurantsettings` SET MaxTables = ? WHERE Day = ? ")){
				
				
				
				pst.setInt(1, r.getMaxTables()); 
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
	     * Updates an existing SpecialDates in the database.
	     *
	     * @param d the SpecialDates object containing updated data
	     * @return true if the update succeeded, false otherwise
	     */
		public Boolean updateSpecialDates(SpecialDates d) {
			
			Connection con = getConnection(); //connect to DB
			
			try (PreparedStatement pst = con.prepareStatement("UPDATE `specialdates` SET OpeningHours = ? , ClosingHours = ? , description = ?   WHERE special_date = ? ")){
				
								
				pst.setTime(1, java.sql.Time.valueOf(d.getOpeningTime()));
				pst.setTime(2, java.sql.Time.valueOf(d.getClosingTime()));
				pst.setString(3, d.getDescription());
				pst.setDate(4, java.sql.Date.valueOf(d.getDate()));
				
				
				
				
				
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
			

		
		
	    /**
	     * Inserts a new bill into the database.
	     *
	     * @param b the bill to insert
	     * @return true if the insertion succeeded, false otherwise
	     */
		public Boolean AddBill(Bill b) {
			
			Connection con = getConnection(); //connect to DB
			
			try (PreparedStatement pst = con.prepareStatement("INSERT INTO `bill` (BillId, reservationID, Amount, issuedAt , paid) VALUES (?,?,?,?,?)")){
				
	            pst.setInt(1, b.getBillID()); 
	            pst.setInt(2, b.getReservationID()); 
	            pst.setDouble(3, b.getTotalAmount()); 
	            pst.setTimestamp(4, java.sql.Timestamp.valueOf(b.getIssuedAt()));
	            pst.setBoolean(5, b.isPaid());
	            
	            
	            
	            int update_status = pst.executeUpdate();
	            return update_status > 0;
	
		        } catch (SQLException e) {
		        	e.printStackTrace();
		        	return false;
		        }
				
			}

		
	    /**
	     * Retrieves a specific bill by their ID.
	     *
	     * @param Billid the bill ID
	     * @return the Bill if found, or null if not found
	     */
		public Bill GetBill(int billId) {

		    Connection con = getConnection();
		    Bill b = null;

		    try (PreparedStatement pst = con.prepareStatement(
		            "SELECT * FROM bill WHERE BillId = ?")) {

		        pst.setInt(1, billId);
		        ResultSet rs = pst.executeQuery();

		        if (rs.next()) {

		            int id = rs.getInt("BillId");
		            int reservationID = rs.getInt("reservationID");
		            double amount = rs.getDouble("Amount");
		            LocalDateTime issuedAt =  rs.getTimestamp("issuedAt").toLocalDateTime();
		            boolean paid = rs.getBoolean("paid");

		            b = new Bill(id, reservationID, amount, issuedAt, paid);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return b; 
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

			
			
		
}

	    	






