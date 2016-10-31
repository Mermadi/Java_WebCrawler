import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class WebCrawler implements Runnable {
	private final String url;
	Document doc;
	Elements links;
	Elements media;
	Elements imports;
	

	public WebCrawler ( String url ){
		this.url = url;
		connectToUrl();
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
		
		// SCRAPE Directly from jSoup docs
		// this scraped content can be stored in a DB rather than printed
		
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

}
