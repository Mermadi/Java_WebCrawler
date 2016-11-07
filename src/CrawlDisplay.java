import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CrawlDisplay {

	private JFrame frame;
	private JTextField UrlsTextField;
	CrawlPool pool;

	public void CrawlScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CrawlDisplay window = new CrawlDisplay();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CrawlDisplay() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 720, 451);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				createCrawlers();
				Utils.connectDatabase ();
				
				String url = UrlsTextField.getText().trim();
				pool.insertURL(url);
				System.out.println(url +" add to queue");
				UrlsTextField.setText("");
				
			}
		});
		btnNewButton.setBounds(552, 29, 117, 30);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnCrawl = new JButton("Crawl");
		btnCrawl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pool.startCrawlers();
			}
		});
		btnCrawl.setBounds(552, 70, 117, 30);
		frame.getContentPane().add(btnCrawl);
		
		UrlsTextField = new JTextField();
		UrlsTextField.setBounds(81, 29, 459, 26);
		frame.getContentPane().add(UrlsTextField);
		UrlsTextField.setColumns(10);
	}
	
	// create pool of crawlers
	public void createCrawlers () {
		pool = new CrawlPool ();
		pool.initCrawlers ();
	}
}