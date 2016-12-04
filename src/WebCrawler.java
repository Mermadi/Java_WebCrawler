import org.jsoup.Jsoup;
import java.sql.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class WebCrawler implements Runnable {
	private String element [];
	private String url,layer, name;
	private Document doc;
	private Elements links,media,imports;
	LinkedBlockingQueue < String [] > SharedUrlPool = new LinkedBlockingQueue< String [] >(100);
	private Set <String> urlsVisited = Collections.synchronizedSet(new HashSet<>(100));
	public static int urlCount = 1;
    private int URLS_LIMIT = 100;
	Connection conn; 

	// constructor
	public WebCrawler ( LinkedBlockingQueue < String [] > SharedUrlPool, String name, Set < String > urlsVisited, int URLS_LIMIT) {
		this.SharedUrlPool = SharedUrlPool;
		this.urlsVisited = urlsVisited;
		this.conn = Utils.connectDatabase(); 
		this.name = name;
		this.URLS_LIMIT = URLS_LIMIT;
	}
	
	// check if connection to url is good and grab html doc with elements
	public boolean connectToUrl ( String url ) {
		boolean success;
		if ( url != null ) {
			try {
				this.doc = Jsoup.connect(url).get();
				this.links = doc.select( "a[href]" );
				this.media = doc.select( "[src]" );
				this.imports = doc.select( "link[href]" );
				success = true;
			} catch ( IOException e ) {
				e.getMessage();
				success = false;
			} catch ( IllegalArgumentException e ) {
				System.out.println( "skipping a malformed url" );
				success = true;
			}
		} else {
			success = false;
		}
		return success;
	}
	
	// update static count variable (tracked by progress bar swing worker thread
	public void updateCount () {
	  urlCount = urlsVisited.size(); 
	   if (urlCount == 100 ){
		   for (int i = 0; i < 100; i++){
		       urlCount = urlsVisited.size(); 
		   }
	   }
	}

	// main thread body
	@Override
	public void run() {
		while ( urlsVisited.size() < URLS_LIMIT  ) {
			 if ( !SharedUrlPool.isEmpty() ) {
				try {
					this.element = SharedUrlPool.take(); //wait here if no elements are in queue
					this.url = this.element[0];
					this.layer = this.element[1];
										
				} catch ( InterruptedException e ) {
					e.printStackTrace();
				}
				
				// before scraping grabbed element ensure these conditions are met
				if ( connectToUrl( this.url ) && urlsVisited.add(this.url) && urlsVisited.size() <= URLS_LIMIT  ) {
					System.out.println(urlsVisited.size() +" "+ name + " scraping " + this.url + " at layer: " + this.layer);
					int newLayer = Integer.parseInt(this.layer) + 1;
					Set < String > foundLinks = ( new HashSet < String > ( 300 ) );
			        
			        for ( Element src : media ) {
			            if ( src.tagName().equals( "img" ) ) {
			        		Utils.writeToDatabase( conn, this.url, src.attr("src"), ( ""+newLayer ), "media" );
			            } 
			        }
		
			        for ( Element imports : imports ) {
			        	Utils.writeToDatabase( conn, this.url, imports.attr( "abs:href" ), ( ""+newLayer ), "imports");
			        }
		
			        for ( Element link : links ) {
			            String element []= { link.attr( "abs:href" ), ( ""+ newLayer ) };
			            if ( (!urlsVisited.contains(element[0])) && foundLinks.add(element[0] ) ){
			            	Utils.writeToDatabase( conn, this.url, element[0], element[1], "links");
		                	SharedUrlPool.offer( element ); //add to tail of shared queue
		                }
			        }
			        updateCount ();
				}
			 } else {
				 try {
					 Thread.sleep( 100 );
				 } catch ( InterruptedException e ) {
					 e.getMessage();
				 }
			}
		}
		Utils.closeDatabaseConnection ( conn );
		 try {
			 Thread.sleep( 5000 );
		 } catch ( InterruptedException e ) {
			 e.getMessage();
		 }
	}
}