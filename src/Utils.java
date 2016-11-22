import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class Utils {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://localhost/crawledInfo?useSSL=false";
	private static final String USER = "agodinez";
	private static final String PASS = "SecretPassword";
	static int numConnections = 0;

	// format string url
	public static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width-1) + ".";
		else
			return s;
	}
	
	// custom print method
	public static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}
	
	// connect to mysql
	public static Connection connectDatabase () {
		Connection conn = null;
	    try {
	    	Class.forName( JDBC_DRIVER );
	        try {
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	    return conn;
	}
	
	// insert into database
	public static boolean writeToDatabase ( Connection conn, String url, String link, String layer ) {
	    boolean success = false;
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("INSERT INTO scraped " + "VALUES ('"+ url +"','"+ link +"','"+ layer +"')");
		    success = true;
		} catch (SQLException e) {
			e.printStackTrace();	
		}
		return success;
	}	
	
	// close database connection
	public static boolean closeDatabaseConnection ( Connection conn) {
	    boolean success = false;
		try {
			conn.close();
		    success = true;
		} catch (SQLException e) {
			e.printStackTrace();	
		}
		return success;
	}
	
	// search database returns ResultSet
	public static ResultSet search ( Connection conn, String url) {
		ResultSet rs = null;
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT url,links,layer FROM scraped WHERE url = '"+ url +"'";
		    rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();	
		}
		return rs;
	}
	
	// get scraped urls as a set to avoid duplicate handling
	public static Set < String > getScrapedUrls ( Connection conn ) {
		Set < String > scrapedURLS = new HashSet <String>  ();
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT url FROM scraped";
		    ResultSet rs = stmt.executeQuery( sql );

		    while( rs.next() ) {
		        String result = rs.getString(1);
		        if (result != null) {
		            result = result.trim();
		        }
		        scrapedURLS.add(result);
		    }

		} catch ( SQLException e ) {
			e.printStackTrace();	
		}
		return scrapedURLS;
	}
}