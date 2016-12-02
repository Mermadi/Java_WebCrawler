import org.jsoup.Jsoup;
import java.sql.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class WebCrawler implements Runnable {
	private String element [];
	private String url,layer;
	private Document doc;
	private Elements links,media,imports;
	LinkedBlockingQueue < String [] > SharedUrlPool = new LinkedBlockingQueue< String [] >(100);
	Set<String> urlsVisited = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    //Set < String > urlsVisited = 	( new HashSet < String > ( 100 ) );
	public static int urlCount = 1;
	public static int exit = 0;
	Connection conn; 

	public WebCrawler ( LinkedBlockingQueue < String [] > SharedUrlPool, String name, Set < String > urlsVisited ) {
		this.SharedUrlPool = SharedUrlPool;
		this.urlsVisited = urlsVisited;
		this.conn = Utils.connectDatabase(); 
	}
	
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
	
	@Override
	public void run() {
		while ( urlsVisited.size() <= 100 ) {
			if ( !SharedUrlPool.isEmpty() ) {
				try {
					this.element = SharedUrlPool.take();
					this.url = this.element[0];
					this.layer = this.element[1];
					
				} catch ( InterruptedException e ) {
					e.printStackTrace();
				}
				if ( connectToUrl( this.url ) ) {
					int newLayer = Integer.parseInt(this.layer) + 1;
					Set < String > foundLinks = ( new HashSet < String > ( 300 ) );
			        
			        for ( Element src : media ) {
			            if ( src.tagName().equals( "img" ) ) {
			        		Utils.writeToDatabase( conn, this.url, src.attr("src"), ( ""+newLayer ), "media" );
			            } 
			        }
		
			        for ( Element link : imports ) {
			        	Utils.writeToDatabase( conn, this.url, link.attr( "abs:href" ), ( ""+newLayer ), "imports");
			        }
		
			        for ( Element link : links ) {
			            String element []= { link.attr( "abs:href" ), ( ""+ newLayer ) };
			            if ( !urlsVisited.contains( element[0]) && foundLinks.add(element[0] ) ){
			            	Utils.writeToDatabase( conn, this.url, element[0], element[1], "links");
			            	SharedUrlPool.offer( element );
			            }
			        }
			        urlsVisited.add( this.url );
		            urlCount = urlsVisited.size();
		            System.out.println(urlCount);
				}
			} else {
				try {
					Thread.sleep( 2000 );
				} catch ( InterruptedException e ) {
					e.getMessage();
				}
			}
		}
		Utils.closeDatabaseConnection ( conn );
		exit++;
		if ( exit == 5 ){
			urlCount = 100;
		}
	}
}