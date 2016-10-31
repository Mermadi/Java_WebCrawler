import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.*;

public class StartCrawl {
	
	public static void main( String[] args ) {
		
		//input urls will later be read from a file or user input
		String url = "http://www.yahoo.com";
		
		final ExecutorService executor = Executors.newFixedThreadPool(5);
	    WebCrawler  one = new WebCrawler( url );
	    
	    executor.execute( one );
	    executor.shutdown();
	}
}
