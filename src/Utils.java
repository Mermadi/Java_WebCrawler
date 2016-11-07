import java.sql.*;

public class Utils {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/crawledInfo";
	static final String USER = "agodinez";
	static final String PASS = "<PASS GOES HERE>";

	public static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width-1) + ".";
		else
			return s;
	}
	
	public static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}
	
	public static Connection connectDatabase () {
		Connection conn = null;
		Statement stmt = null;
	    try {
	    	Class.forName("com.mysql.jdbc.Driver");
	    	System.out.println("Connecting to a selected database...");
	        try {
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
		        System.out.println("Connected database successfully...");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	    return conn;
	}
	
}
