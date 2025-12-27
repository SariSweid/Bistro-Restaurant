package DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import Entities.Bill;
import Entities.Guest;
import Entities.Report;
import Entities.Reservation;
import Entities.Table;
import Entities.User;





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
	    			Entities.Reservation.Status status = Entities.Reservation.Status.valueOf(rs.getString("status"));
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
	    			Entities.Reservation.Status status = Entities.Reservation.Status.valueOf(rs.getString("status"));
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
	     *
	     * @param r the reservation to insert
	     * @return true if the insertion succeeded, false otherwise
	     */
	    public boolean insertReservation(Reservation r) {
	        Connection con = getConnection(); 
	        
	        try (PreparedStatement pst = con.prepareStatement(
	            "INSERT INTO `reservation`  (reservationID, customerID, tableId, billId, numOfGuests, confirmationCode,  reservationDate, reservationTime, reservationPlacedDate, reservationPlacedTime, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
	        )) {

	            pst.setInt(1, r.getReservationID());
	            pst.setInt(2, r.getCustomerId());
	            pst.setInt(3, r.getTableID());
	            pst.setInt(4, r.getBillID());
	            pst.setInt(5, r.getNumOfGuests());
	            pst.setInt(6, r.getConfirmationCode());
	            pst.setDate(7, java.sql.Date.valueOf(r.getReservationDate()));
	            pst.setTime(8, java.sql.Time.valueOf(r.getReservationTime()));
	            pst.setDate(9, java.sql.Date.valueOf(r.getReservationPlacedDate()));
	            pst.setTime(10, java.sql.Time.valueOf(r.getReservationPlacedTime()));
	            pst.setString(11, r.getStatus().name());

	            int update_status = pst.executeUpdate();
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
	        	
	            pst.setInt(1, u.getuserId());
	            pst.setString(2, u.getName()); // not exist yet
	            pst.setString(3, u.getPhone());
	            pst.setString(4, u.getEmail());
	            pst.setInt(5, u.getuserId());
	            pst.setInt(6, u.getMemberShipCode()); // not exist yet
	            pst.setString(7, u.getRole().name());
	        	        
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
				
	        	pst.setString(1, u.getName()); // not exist yet
				pst.setString(2, u.getPhone());  
	            pst.setString(3, u.getEmail());
  	            pst.setString(4, u.getuserName());  // not exist yet
  	            pst.setInt(5, u.getuserId());
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
    	
    	List<User> Users = new ArrayList<>(); // made new list to return
    	Connection con = getConnection(); //connect to DB
    	
    	try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `user`")){ // ask from DB the all Orders
    		
    		ResultSet rs = pst.executeQuery();
    		
    		while(rs.next()) { // read from DB
    			
    			int UserId  = rs.getInt("UserId");
    			String Name  = rs.getString("Name"); 
    			String Phone  = rs.getString("Phone");
    			String Email  = rs.getString("Email");
    			String UserName  = rs.getString("UserName");
    			int SubscriberMemberShipCode = rs.getInt("MemberShipCode");
    			Entities.User.Role Role = Entities.User.Role.valueOf(rs.getString("Role"));

    			
                User u;

//                switch (Role) {    // will update this when the classes will be complete
//                    case GUEST:
//                        u = new Guest(userId, email, phone);
//                        break;
//                    case SUBSCRIBER:
//                        u = new Subscriber(userId, email, phone);
//                        break;
//                    case SUPERVISOR:
//                        u = new RestaurantSupervisor(userId, email, phone);
//                        break;
//                    case MANAGER:
//                        u = new RestaurantManager(userId, email, phone);
//                        break;
//                    default:
//                        continue;
//                }

  //              Users.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Users;
	    }

	    
	    
	    /**
	     * Retrieves a specific user by their ID.
	     *
	     * @param userId the user ID
	     * @return the User if found, or null if not found
	     */
		public User GetUser(int userId) {
			
			Connection con = getConnection(); //connect to DB
			User u = null;
			
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `user` WHERE UserId = ?")){ // ask from DB the User that we want
				
				pst.setInt(1, userId);
				ResultSet rs = pst.executeQuery();
				
		        if (rs.next()) {
	        	
    			int UserId  = rs.getInt("UserId");
    			String name  = rs.getString("Name"); 
    			String phone  = rs.getString("Phone"); 
    			String email  = rs.getString("Email");
    			String username  = rs.getString("UserName");
    			int MemberShipCode = rs.getInt("MemberShipCode");
    			Entities.User.Role role = Entities.User.Role.valueOf(rs.getString("Role")); 
    			
    			switch (role) {
    		    case GUEST:
    		        u = new Guest(UserId, email, phone);
    		        break;

//    		    case SUBSCRIBER: // will update this when the classes will be complete
//    		        u = new Subscriber(UserId, email, phone, MemberShipCode);
//    		        break;
//
//    		    case SUPERVISOR:
//    		        u = new RestaurantSupervisor(UserId, email, phone);
//    		        break;
//
//    		    case MANAGER:
//    		        u = new RestaurantManager(UserId, email, phone);
//    		        break;
    		}
		        }
			}
			
			 catch (SQLException e) {
				e.printStackTrace();
			}
			
			return u;
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

		        pst.setInt(1, user.getuserId());
		        ResultSet rs = pst.executeQuery();

		        while (rs.next()) {

		            int reservationID = rs.getInt("reservationID");
		            Date reservationDate = rs.getDate("reservationDate");
		            Time reservationTime = rs.getTime("reservationTime");
		            int numOfGuests = rs.getInt("numOfGuests");
		            int confirmationCode = rs.getInt("confirmationCode");
		            Reservation.Status status = Reservation.Status.valueOf(rs.getString("status"));
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
				
		        	t = new Table(tableId , capacity , isAvailable );
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
				
				pst.setBoolean(1, t.getIsAvailable()); // not exist yet
				pst.setInt(2, t.getTableId()); // not exist yet
				
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
				
				pst.setInt(1, t.getTableId()); // not exist yet
				
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
		public Boolean AddReport(Report r) {
			
			Connection con = getConnection(); //connect to DB
			
			try (PreparedStatement pst = con.prepareStatement("INSERT INTO `report` (Report_Id, Type, From, To) VALUES (?,?,?,?)")){
				
	            pst.setInt(1, r.getReportId()); // not exist yet
	            pst.setString(2, r.getType()); // not exist yet 
	            pst.setDate(3, r.getFrom()); // not exist yet
	            pst.setDate(4, r.getTo()); // not exist yet

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
    			
	    			int tableId  = rs.getInt("Report_Id");// not exist yet
	    			String Type  = rs.getString("Type"); // not exist yet
	    			Date from = rs.getDate("From"); // not exist yet
	    			Date to = rs.getDate("To"); // not exist yet
	    			
	    			
	    			
	    			Report r = new Report(tableId , Type , from , to  ); // not exist yet
	    			reports.add(r);
	    		
	    		}			
			} catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	 return reports;		
		}
	
	
		
		
		
//		public Boolean updateOpeningHours() {
//			
//		}
//		
//		public Boolean updateClosingHours() {
//			
//		}
//		
//		public Boolean updateMaxTable() {
//			
//		}
//		
//		public Boolean updateSpecialDates() {
//			
//		}
//
	    /**
	     * Inserts a new SpecailDates into the database.
	     *
	     * @param d the SpecailDates to insert
	     * @return true if the insertion succeeded, false otherwise
	     */
		public Boolean addSpecialDates(SpecailDates d) {
			Connection con = getConnection(); //connect to DB
			
			try (PreparedStatement pst = con.prepareStatement("INSERT INTO `specialdates` (special_date, OpeningHours, ClosingHours, description) VALUES (?,?,?,?)")){  
				
	            pst.setDate(1, d.special_date()); // not exist yet
	            pst.setTime(2, d.getOpeningHours()); // not exist yet 
	            pst.setTime(3, d.getClosingHours()); // not exist yet
	            pst.setString(4 ,d.getdescription()); // not exist yet	
	            
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
			
			try (PreparedStatement pst = con.prepareStatement("INSERT INTO `bill` (BillId, Amount, User_Id, Items) VALUES (?,?,?,?)")){
				
	            pst.setInt(1, b.getBillId()); // not exist yet
	            pst.setInt(2, b.getAmount()); // not exist yet 
	            pst.setInt(3, b.getUserId()); // not exist yet
	            pst.setArray(4, b.getItems()); // not exist yet
	            
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
		public Bill GetBill(int Billid) {
			
			Connection con = getConnection();
			Bill b = null;
			
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `bill` WHERE BillId = ?")){
				
				pst.setInt(1, Billid);
				ResultSet rs = pst.executeQuery();
				
		        if (rs.next()) {
		        	
		        	int BillId = rs.getInt("BillId");
		        	int amount = rs.getInt("Amount");
		        	int UserId = rs.getInt("User_Id");	
		        	@SuppressWarnings("unchecked")
					List <String> items = (List<String>) rs.getArray("Items");
			
		        	b = new Bill(BillId , amount , UserId , items );
		        }
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
	
			return b; // There isnt Res with this ID.
			}
		
		
		
		
		
	    /**
	     * Inserts a new Reservation into the database.
	     *
	     * @param r the Reservation to insert
	     * @return true if the insertion succeeded, false otherwise
	     */
			public Boolean addToWitingList(Reservation r) { // not sure if i recive  Reservation Or User
				
				Connection con = getConnection();
				try (PreparedStatement pst = con.prepareStatement("INSERT INTO `waitinglist` (Customer, NumOfGuests, ConfirmationCode) VALUES (?,?,?)")){
					
		            pst.setInt(1, r.getCustomerId() ); 
		            pst.setInt(2, r.getNumOfGuests()); 
		            pst.setInt(3, r.getConfirmationCode()); 
		            
		            int update_status = pst.executeUpdate();
		            return update_status > 0;
		
			        } catch (SQLException e) {
			        	e.printStackTrace();
			        	return false;
			        }
					
				}
			
			
			
		    /**
		     * Remove an existing Reservation from the database.
		     *
		     * @param r the Reservation object containing updated data
		     * @return true if the Remove succeeded, false otherwise
		     */
			public Boolean removeFromWitingList(Reservation r) { // not sure if i recive  Reservation Or User
				
				Connection con = getConnection(); //connect to DB
				
				try (PreparedStatement pst = con.prepareStatement("DELETE FROM `waitinglist` WHERE Customer = ? ")){
					
					pst.setInt(1, r.getCustomerId()); // not exist yet
					
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
			       PreparedStatement pst1 = con.prepareStatement("SELECT Customer, NumOfGuests FROM waitinglist LIMIT 1"); // first we  check if there is waitings
			        ResultSet rs = pst1.executeQuery();

			        if (!rs.next()) 
			            return false; 
			        
			        int customerId = rs.getInt("Customer");
			        int numGuests = rs.getInt("NumOfGuests");
			        

	
			        PreparedStatement pst2 = con.prepareStatement("SELECT TableId FROM `table`  WHERE IsAvailable=1 AND capacity >= ? ORDER BY capacity LIMIT 1"); // than we search for him a table
			        pst2.setInt(1, numGuests);

			        ResultSet rs2 = pst2.executeQuery();
			        if (!rs2.next()) 
			           return false; 
			        

			        int tableId = rs2.getInt("table_id");

			        PreparedStatement pst3 = con.prepareStatement("DELETE FROM waitinglist WHERE Customer=?"); // we remove him from the waiting list
			        pst3.setInt(1, customerId);
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

	    	






