public class Utils {
	
	// this class will serve to hold utility methods that WebCrawlers have access to
	
	public static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width-1) + ".";
		else
			return s;
	}
	
	public static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}
	
}
