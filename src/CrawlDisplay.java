import java.awt.EventQueue;

import javax.swing.JFrame;

public class CrawlDisplay {

	private JFrame frame;

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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}