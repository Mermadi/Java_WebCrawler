import java.util.concurrent.Executors;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;


public class StartCrawl {
	
	public static void main( String[] args ) {
		
		// input urls will later be read from a file or user input
		String url1 = "http://www.yahoo.com";
		String url2 = "http://www.google.com";
		String url3 = "http://www.github.com";

		BlockingQueue < String > SharedUrlPool = new ArrayBlockingQueue< String >( 10 );
		final ExecutorService executor = Executors.newFixedThreadPool(5);
		WebCrawler[] crawlers = new WebCrawler [ 6 ];
		
		// dummy crawler to sit inside first index
		crawlers [0] = new WebCrawler( SharedUrlPool, "Dummy");
		
		// produce urls into shared queue
		SharedUrlPool.offer(url1);
		SharedUrlPool.offer(url2);
		SharedUrlPool.offer(url3);

		// create 5 WebCrawler instances 
		for ( int count = 1; count < 6 ; count++ ) {
		crawlers [ count ] = new WebCrawler( SharedUrlPool, "[ Crawler "+ count +" ]" );
		}
	    
		// execute the 5 WebCrawler instances in new threads
		for ( int count = 1; count < 6 ; count++ ) {
			executor.execute( crawlers [ count ] );
		}
	    executor.shutdown();
	}
}