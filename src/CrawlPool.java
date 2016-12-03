import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class CrawlPool {
	
	LinkedBlockingQueue < String [] > SharedUrlPool = new LinkedBlockingQueue< String [] >(100);
	Set <String> urlsVisited = Collections.synchronizedSet(new HashSet<>(100));
	final ExecutorService executor = Executors.newFixedThreadPool(5);
	WebCrawler[] crawlers = new WebCrawler [ 6 ];

	// insert single url into pool
	public boolean insertURL ( String url ){
		String element []= {url, "0"};
		if ( Utils.connectToUrl(url)){
			SharedUrlPool.offer(element);
			return true;
		} else {
			return false;
		}
	}

	// create 5 WebCrawler instances 
	public void initCrawlers () {
		for ( int count = 1; count < 6 ; count++ ) {
			crawlers [ count ] = new WebCrawler( SharedUrlPool, "[ Crawler "+ count +" ]", urlsVisited );
		}	
	}
	
	// execute the 5 WebCrawler instances in new threads
	public void startCrawlers (){
		for ( int count = 1; count < 6 ; count++ ) {
			executor.execute( crawlers [ count ] );
		}
	}
	
	// shutdown thread pool
	public void shutdownPool () {
	    executor.shutdown();
	}
}