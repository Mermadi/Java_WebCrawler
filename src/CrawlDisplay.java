import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class CrawlDisplay {

	private JFrame frame;
	private JTextField UrlsTextField;
	JProgressBar progressBar;
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
		frame = new JFrame("Crawler");
		frame.setBounds(100, 100, 722, 153);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	    frame.setLocationRelativeTo(null);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				createCrawlers();
				String url = UrlsTextField.getText().trim();
				pool.insertURL(url);
				WebCrawler.urlCount = 1;
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
				ProgressWorker pw = new ProgressWorker();
				pw.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						String name = evt.getPropertyName();
                        if (name.equals("progress")) {
                            int progress = (int) evt.getNewValue();
                            progressBar.setValue(progress);
                            progressBar.repaint();
                        } 
					}
				});
				pw.execute();
			}
		});
		
		btnCrawl.setBounds(552, 70, 117, 30);
		frame.getContentPane().add(btnCrawl);
		
		UrlsTextField = new JTextField();
		UrlsTextField.setBounds(81, 29, 459, 26);
		frame.getContentPane().add(UrlsTextField);
		UrlsTextField.setColumns(10);
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setBounds(136, 70, 352, 30);
		frame.getContentPane().add(progressBar);
		
		JLabel lblUrl = new JLabel("URL");
		lblUrl.setBounds(50, 35, 34, 16);
		frame.getContentPane().add(lblUrl);

	}
	
	// updates progress bar in the background 
    public class ProgressWorker extends SwingWorker<Object, Object> {
        @Override
        protected Object doInBackground() throws Exception {
        	   while ( WebCrawler.urlCount < 100 ) {        
                   setProgress( WebCrawler.urlCount );
                   System.out.println( "*******" + WebCrawler.urlCount );
                   try {
                       Thread.sleep( 45 );
                   } catch ( Exception e ) {
                       e.printStackTrace();
                   }
               }
            return null;
        }
    }
	
	// create pool of crawlers
	public void createCrawlers () {
		pool = new CrawlPool ();
		pool.initCrawlers ();
	}
}