import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainMenu {

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainMenu() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Menu");
		frame.setBounds(100, 100, 162, 94);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.setBounds(22, 6, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		JButton button = new JButton("Crawl");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CrawlDisplay cd = new CrawlDisplay();
				cd.CrawlScreen();
				
			}
		});
		button.setBounds(22, 37, 117, 29);
		frame.getContentPane().add(button);
	}
}
