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
import javax.swing.JOptionPane;

public class CrawlDisplay {

	JFrame frame;
	private JTextField UrlsTextField;
	JProgressBar progressBar;
	CrawlPool pool;
	ProgressWorker pw = new ProgressWorker();
	CrawlDisplay window;
	private static boolean progressStarted = false;

	public void CrawlScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new CrawlDisplay();
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
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	    frame.setLocationRelativeTo(null);
	    
	    frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	    		WebCrawler.urlCount = 0;
	    		WebCrawler.exit = 0;
	        }
	    });
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				createCrawlers();
				String url = UrlsTextField.getText().trim();
				if (!pool.insertURL(url)) {
              	   JOptionPane.showMessageDialog(frame, "Couldn't connect to "+ url);
				}
				UrlsTextField.setText("");		
			}
		});
		
		btnNewButton.setBounds(552, 29, 117, 30);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnCrawl = new JButton("Crawl");
		btnCrawl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pool.startCrawlers();
				if ( progressStarted == false ) { 
					startProgressListener();
				}
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
        	   while ( WebCrawler.urlCount <= 120 ) {        
                   setProgress( WebCrawler.urlCount );
                   try {
                       Thread.sleep( 500 );
                   } catch ( InterruptedException e ) {
                	   return null;
                   }
               }
            return null;
        }
    }
    
    public void startProgressListener () {
		pw.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String name = evt.getPropertyName();
                if (name.equals("progress")) {
                    int progress = (int) evt.getNewValue();
                    System.out.println ("Progress: " + progress);
                    if ( progress >= 100 ){
                 	   JOptionPane.showMessageDialog(frame, "Scrape Complete");
                 	   pool.shutdownPool();
                    }
                    progressBar.setValue(progress);
                    progressBar.repaint();
                } 
			}
		});
		pw.execute();
		progressStarted = true;
    }
    
	// create pool of crawlers
	public void createCrawlers () {
		pool = new CrawlPool ();
		pool.initCrawlers ();
		WebCrawler.urlCount = 0;
		WebCrawler.exit = 0;
	}
}