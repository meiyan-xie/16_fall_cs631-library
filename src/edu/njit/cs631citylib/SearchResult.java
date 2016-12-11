package edu.njit.cs631citylib;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

public class SearchResult extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public String searchKeyword;
	public int searchType;
	public final int SEARCH_TYPE_ID = 1;
	public final int SEARCH_TYPE_TITLE = 2;
	public final int SEARCH_TYPE_PUBLISHER = 3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SearchResult dialog = new SearchResult();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SearchResult() {
		// Initialize MySQL connection
		DBManager m = DBManager.getInstance();
		
		setBounds(100, 100, 1000, 610);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JList listDoc = new JList();
		listDoc.setBounds(71, 121, 868, 346);
		contentPanel.add(listDoc);
		
		JLabel lblNewLabel = new JLabel("Doc search result");
		lblNewLabel.setBounds(74, 78, 853, 31);
		contentPanel.add(lblNewLabel);
		
		JButton btnBorrow = new JButton("CHECK OUT");
		btnBorrow.setBounds(112, 505, 117, 29);
		contentPanel.add(btnBorrow);
		
		JButton btnReserve = new JButton("RESERVE");
		btnReserve.setBounds(460, 505, 117, 29);
		contentPanel.add(btnReserve);
		
		
		
		
	}
}
