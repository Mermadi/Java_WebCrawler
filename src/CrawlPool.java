import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class CrawlPool {
	
	
	private final int URLS_LIMIT = 100;
	private final int CRAWLER_THREADS = 5;
	
	LinkedBlockingQueue < String [] > SharedUrlPool = new LinkedBlockingQueue< String [] >(URLS_LIMIT);
	Set <String> urlsVisited = Collections.synchronizedSet(new HashSet<>(URLS_LIMIT));
	final ExecutorService executor = Executors.newFixedThreadPool(CRAWLER_THREADS);
	WebCrawler[] crawlers = new WebCrawler [ CRAWLER_THREADS + 1 ];

	// insert root url into pool
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
		for ( int count = 1; count <= CRAWLER_THREADS ; count++ ) {
			crawlers [ count ] = new WebCrawler( SharedUrlPool, "[ Crawler "+ count +" ]", urlsVisited, URLS_LIMIT );
		}	
	}
	
	// execute the 5 WebCrawler instances in new threads
	public void startCrawlers (){
		for ( int count = 1; count <= CRAWLER_THREADS ; count++ ) {
			executor.execute( crawlers [ count ] );
		}
	}
	
	// shutdown thread pool
	public void shutdownPool () {
	    executor.shutdown();
	}
}