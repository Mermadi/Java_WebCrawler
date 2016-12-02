import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import net.proteanit.sql.DbUtils;
import javax.swing.JComboBox;

public class SearchDisplay {

	private JFrame frame;
	CrawlPool pool;
	private JTable tableLinks;
	private JTable tableMedia;
	private JTable tableImports;
	Connection conn;

	public void SearchScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchDisplay window = new SearchDisplay();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SearchDisplay() {
		conn = Utils.connectDatabase ();
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Searcher");
		frame.setBounds(100, 100, 845, 399);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	    frame.setLocationRelativeTo(null);

		JScrollPane scrollPaneLinks = new JScrollPane();
		scrollPaneLinks.setBounds(30, 105, 253, 246);
		frame.getContentPane().add(scrollPaneLinks);
		
		JScrollPane scrollPaneMedia = new JScrollPane();
		scrollPaneMedia.setBounds(295, 105, 253, 246);
		frame.getContentPane().add(scrollPaneMedia);
		
		JScrollPane scrollPaneImports = new JScrollPane();
		scrollPaneImports.setBounds(560, 105, 253, 246);
		frame.getContentPane().add(scrollPaneImports);
		
		tableLinks = new JTable();
		scrollPaneLinks.setViewportView(tableLinks);
		
		tableMedia = new JTable();
		scrollPaneMedia.setViewportView(tableMedia);
		
		tableImports = new JTable();
		scrollPaneImports.setViewportView(tableImports);
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		JComboBox comboBox = new JComboBox ( Utils.getScrapedUrls(conn).toArray() );
		comboBox.setEditable(true);
		comboBox.setBounds(30, 30, 459, 27);
		frame.getContentPane().add(comboBox);
		comboBox.setSelectedItem("");
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String url = (String) comboBox.getSelectedItem();
				
				ResultSet  rsLinks = Utils.search ( conn, url, "links" );
				tableLinks.setModel(DbUtils.resultSetToTableModel(rsLinks));
				
				ResultSet  rsMedia = Utils.search ( conn, url, "media" );
				tableMedia.setModel(DbUtils.resultSetToTableModel(rsMedia));
				
				ResultSet  rsImports = Utils.search ( conn, url, "imports" );
				tableImports.setModel(DbUtils.resultSetToTableModel(rsImports));
			}
		});
		btnNewButton.setBounds(501, 29, 117, 30);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblResults = new JLabel("Results");
		lblResults.setBounds(30, 77, 61, 16);
		frame.getContentPane().add(lblResults);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(81, 81, 731, 12);
		frame.getContentPane().add(separator);
	}	
}