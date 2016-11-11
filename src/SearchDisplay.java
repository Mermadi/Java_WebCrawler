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
	private JTable table;
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
		frame.setBounds(100, 100, 722, 399);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	    frame.setLocationRelativeTo(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(81, 105, 588, 246);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		JComboBox comboBox = new JComboBox ( Utils.getScrapedUrls(conn).toArray() );
		comboBox.setEditable(true);
		comboBox.setBounds(81, 31, 459, 27);
		frame.getContentPane().add(comboBox);
		comboBox.setSelectedItem("");
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String url = (String) comboBox.getSelectedItem();
				comboBox.setSelectedItem("");
				ResultSet rs = Utils.search ( conn, url);
			    table.setModel(DbUtils.resultSetToTableModel(rs));
			}
		});
		btnNewButton.setBounds(552, 29, 117, 30);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblResults = new JLabel("Results");
		lblResults.setBounds(81, 77, 61, 16);
		frame.getContentPane().add(lblResults);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(132, 81, 537, 12);
		frame.getContentPane().add(separator);
	}	
}