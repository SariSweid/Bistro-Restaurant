package DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Entities.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;





public class DBController {


	    private static Connection conn = null; 
	    private static long lastUsed = 0;
	    private static final long TIMEOUT = 30 * 60 * 1000; // 30 min 

	    // connect to data_base
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
	                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/order?serverTimezone=Asia/Jerusalem&useSSL=false","root","Root1234");
	                System.out.println("SQL connection initialized");	               	                	                
	            }
	            lastUsed = System.currentTimeMillis();
	        }
	            
	        catch (SQLException e) {
	        	e.printStackTrace();
	        }	        
	        return conn;
	    }
	    
	    
	    
	    // func that update the order in the DB 
	    public boolean updateReservation(int reservationId, Date newDate, int newNumGuests) {
	    	Connection con = getConnection(); 
	    	
	        try (PreparedStatement pst = con.prepareStatement("UPDATE `order` SET order_date = ? , number_of_guests = ?  WHERE order_number = ?")) {
	            pst.setDate(1, newDate);      
	            pst.setInt(2, newNumGuests); 
	            pst.setInt(3, reservationId);
	            

	            int update_status = pst.executeUpdate();/////
	            
	            // we return whether the update succeeded or not
	            return update_status > 0;


	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	   }
	    
	    //func that return all the orders from DB
	    public List<Reservation> readAllReservations() {
	    	
	    	List<Reservation> reservations = new ArrayList<>(); // made new list to return
	    	Connection con = getConnection(); //connect to DB
	    	
	    	try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `order`")){ // ask from DB the all Orders
	    		
	    		ResultSet rs = pst.executeQuery();
	    		
	    		while(rs.next()) { // read from DB
	    			
	    			int order_number = rs.getInt("order_number");
	    			Date order_date = rs.getDate("order_date"); 
	    			int number_of_guests = rs.getInt("number_of_guests");
	    			int confirmation_code = rs.getInt("confirmation_code");
	    			int user_id = rs.getInt("user_id");
	    			Date date_of_placing_order = rs.getDate("date_of_placing_order"); 
	    			
	                Reservation r = new Reservation(order_number,order_date,date_of_placing_order,number_of_guests,confirmation_code,user_id);
	                
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




	    //func that insert Reservation into the DB
		public boolean insertReservation(Reservation r) {
			Connection con = getConnection(); 
			
			try (PreparedStatement pst = con.prepareStatement("INSERT INTO `order` (order_number, order_date, number_of_guests,confirmation_code,user_id,date_of_placing_order) VALUES (?, ?, ? ,?, ?, ?)")){
				
	            pst.setInt(1, r.getReservationID());      
	            pst.setDate(2, (Date) r.getReservationDate()); 
	            pst.setInt(3 , r.getNumOfGuests());
	            pst.setInt(4, r.getConfirmationCode()); 
	            pst.setInt(5, r.getCustomerID());
	            pst.setDate(6,(Date) r.getReservationPlacedDate()); 
	            
	            int update_status = pst.executeUpdate();
	            
	            // we return whether the update succeeded or not
	            return update_status > 0;				
			}
			
        	catch (SQLException e) {
        		e.printStackTrace();
            return false;
          }
		}

		
		
//		public Boolean InsertUser(User u) {
//			
//		}
//		
//		public Boolean UpdateUser(User u) {
//			
//		}
//		
//		public List<User> readAllUsers() {
//			
//		}
//		
//		public User GetUser(User u) {
//			
//		}
		
		public Reservation GetReservation(int ReservationId) {
			
			Connection con = getConnection(); //connect to DB
			
			try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `order` WHERE order_number = ?")){
				pst.setInt(1, ReservationId);
				ResultSet rs = pst.executeQuery();
				
		        if (rs.next()) {
		        	
		            int orderNumber = rs.getInt("order_number");
		            java.sql.Date orderDate = rs.getDate("order_date");
		            int numberOfGuests = rs.getInt("number_of_guests");
		            int confirmationCode = rs.getInt("confirmation_code");
		            int userId = rs.getInt("user_id");
		            java.sql.Date dateOfPlacingOrder = rs.getDate("date_of_placing_order");
		        

		            return new Reservation(orderNumber, orderDate,dateOfPlacingOrder, numberOfGuests, confirmationCode, userId);
		        }
		        	        
			}
						
			 catch (SQLException e) {
				e.printStackTrace();
			}
			
			return null; // There isnt Res with this ID.
		}
		
//		public List<Reservation> GetAllUserReservations(User u) {
//			
//		}
		
//		public Table GetTable(Table t) {
//			
//		}
//		
//		public List<Table> GetAllTables(){
//			
//		}
//		
//		public Boolean UpdateTable(Table t) {
//			
//		}
//		
//		public Boolean DeleteTable(Table t) {
//			
//		}
//		
//		public Boolean AddReport(Report r) {
//			
//		}
//		
//		public List<Report> GetAllReports() {
//			
//		}
//		
//		public Bill GetBill(Bill b) {
//			
//		}
		
		
		
		
}

	    	






