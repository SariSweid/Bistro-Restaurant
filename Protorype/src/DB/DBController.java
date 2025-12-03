package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;





public class DBController {


	    private static Connection conn = null;

	    // connect to data_base
	    public static Connection getConnection() {
	        if (conn == null) {
	            try {
	                
	                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/order?serverTimezone=Asia/Jerusalem&useSSL=false","root","Root1234");
	                System.out.println("SQL connection initialized");
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return conn;
	    }

	
	}




