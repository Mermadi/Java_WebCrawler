import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


public class WebCrawler implements Runnable {
	private String element [];
	private String url;
	private String layer;
	private Document doc;
	private Elements links;
	private Elements media;
	private Elements imports;
	LinkedBlockingQueue < String [] > SharedUrlPool = new LinkedBlockingQueue< String [] >( 100 );
    Set < String > urlsVisited = new HashSet <String > ( 100 );
	private String name;
	public static int urlCount = 1;
	

	public WebCrawler ( LinkedBlockingQueue <String []> SharedUrlPool, String name, Set < String > urlsVisited ){
		this.SharedUrlPool = SharedUrlPool;
		this.urlsVisited = urlsVisited;
		this.name = name;
		System.out.println( name + " started..." );
	}
	
	public boolean connectToUrl ( String url) {
		boolean success;
		if ( url != null ) {
			try {
				this.doc = Jsoup.connect(url).get();
				this.links = doc.select("a[href]");
				this.media = doc.select("[src]");
				this.imports = doc.select("link[href]");
				success = true;
			} catch (IOException e) {
				e.getMessage();
				success = false;
			}
		} else {
			success = false;
		}
		return success;
	}
	

	@Override
	public void run() {
		
		while ( urlsVisited.size() <= 95){
			if (!SharedUrlPool.isEmpty()) {
				try {
					this.element = SharedUrlPool.take();
					this.url = this.element[0];
					this.layer = this.element[1];
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (connectToUrl(this.url) ) {
					System.out.println("URL:  " + this.url);
					System.out.println("LAYER:  " + this.layer);
					int newLayer = Integer.parseInt(this.layer) + 1;
			        
					System.out.println(name);
					Utils.print("Media: (%d)", media.size());
			        for (Element src : media) {
			            if (src.tagName().equals("img"))
			            	Utils.print("LAYER:  " + newLayer+ "  * %s: <%s> %sx%s (%s)",
			                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
			                        Utils.trim(src.attr("alt"), 20));
			            else
			            	Utils.print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
			        }
		
			        Utils.print("\nImports: (%d)", imports.size());
			        for (Element link : imports) {
			        	System.out.print("LAYER:  " + newLayer );
			        	Utils.print("  * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
			        }
		
			        Utils.print("\nLinks: (%d)", links.size());
			        for (Element link : links) {
			        	System.out.print("LAYER:  " + newLayer );
			            Utils.print("  * a: <%s>  (%s)", link.attr("abs:href"), Utils.trim(link.text(), 35));
			            String element []= { link.attr( "abs:href" ), ( ""+ newLayer ) };
			            
			            if ( !urlsVisited.contains( element[0] ) ){
			            	 SharedUrlPool.offer( element );
			            }
			        }
			        urlCount++;
		            urlsVisited.add(this.url); 
		            int count = 1;
		            for ( String s : urlsVisited){
		            	System.out.println(count+ ", " +s);
		            	count++;
		            }
				}
			} else {
				try {
					Thread.sleep( 1000 );
				} catch ( InterruptedException e ) {
					e.getMessage();
				}
			}
		}
	}//end run
}