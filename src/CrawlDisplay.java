import java.awt.EventQueue;
import java.awt.Toolkit;
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

public class CrawlDisplay implements PropertyChangeListener, ActionListener {

	JFrame frame;
	private JTextField UrlsTextField;
	JProgressBar progressBar;
	CrawlPool pool;
	CrawlDisplay window;
	private JButton btnCrawl;
	private ProgressWorker pw;

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
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				createCrawlers();
				String url = UrlsTextField.getText().trim();
				if (!pool.insertURL(url)) {
              	   JOptionPane.showMessageDialog(frame, "Couldn't connect to "+ url);
				} else {
	               JOptionPane.showMessageDialog(frame, "Added "+ url);
				}
			}
		});
		
		btnNewButton.setBounds(552, 29, 117, 30);
		frame.getContentPane().add(btnNewButton);
		btnCrawl = new JButton("Crawl");
		btnCrawl.setActionCommand("crawl");
		btnCrawl.addActionListener(this);

		
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
	
	class ProgressWorker extends SwingWorker<Void, Void> {
	    
	     //Main task. Executed in background thread.
	    @Override
	    public Void doInBackground() {
	      // Initialize progress property.
	      WebCrawler.urlCount = 0 ;
	      while (WebCrawler.urlCount < 100) {
	        try {
	          Thread.sleep(500);
	        } catch (InterruptedException ignore) {
	        }
	        setProgress(WebCrawler.urlCount);
	      }
	      return null;
	    }

	     //Executed in event dispatching thread
	    @Override
	    public void done() {
	      Toolkit.getDefaultToolkit().beep();
	      btnCrawl.setEnabled(true);
    	  JOptionPane.showMessageDialog(frame, "Scrape Complete");
    	  setProgress(0);
    	  pool.shutdownPool();
		  UrlsTextField.setText("");		
	    }
	  }
    
	  public void actionPerformed(ActionEvent evt) {
		  	btnCrawl.setEnabled(false);
		  	pool.startCrawlers();
		    // Instances of javax.swing.SwingWorker are not reusuable, so we create new instances as needed.
		    pw = new ProgressWorker();
		    pw.addPropertyChangeListener(this);
		    pw.execute();
	  }

	   // Invoked when task's progress property changes.
	  public void propertyChange(PropertyChangeEvent evt) {
	    if ("progress" == evt.getPropertyName()) {
	      int progress = (Integer) evt.getNewValue();
	      progressBar.setValue(progress);
	    }
	  }
	
	// create pool of crawlers
	public void createCrawlers () {
		pool = new CrawlPool ();
		pool.initCrawlers ();
	}
}