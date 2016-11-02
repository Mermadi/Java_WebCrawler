import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WebCrawler implements Runnable {
	private String url;
	private Document doc;
	private Elements links;
	private Elements media;
	private Elements imports;
	BlockingQueue <String> SharedUrlPool = new ArrayBlockingQueue<String>(10);
	private String name;
	

	public WebCrawler ( BlockingQueue <String> SharedUrlPool, String name ){
		this.SharedUrlPool = SharedUrlPool;
		this.name = name;
	}
	
	public void connectToUrl () {
		try {
			this.doc = Jsoup.connect(this.url).get();
			this.links = doc.select("a[href]");
			this.media = doc.select("[src]");
			this.imports = doc.select("link[href]");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		while ( SharedUrlPool.size() != 0 ){
			this.url  = SharedUrlPool.poll();
			connectToUrl();
	        
			System.out.println(name);
			Utils.print("Media: (%d)", media.size());
	        for (Element src : media) {
	            if (src.tagName().equals("img"))
	            	Utils.print(" * %s: <%s> %sx%s (%s)",
	                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
	                        Utils.trim(src.attr("alt"), 20));
	            else
	            	Utils.print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
	        }

	        Utils.print("\nImports: (%d)", imports.size());
	        for (Element link : imports) {
	        	Utils.print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
	        }

	        Utils.print("\nLinks: (%d)", links.size());
	        for (Element link : links) {
	            Utils.print(" * a: <%s>  (%s)", link.attr("abs:href"), Utils.trim(link.text(), 35));
	        }   
		}
		System.out.println( name + " is exiting" );
	}//end run
}