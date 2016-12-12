package edu.njit.cs631citylib;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class SearchResult extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public String searchKeyword;
	public int searchType;

	public static final int SEARCH_TYPE_ID = 1;
	public static final int SEARCH_TYPE_TITLE = 2;
	public static final int SEARCH_TYPE_PUBLISHER = 3;
	private JTable tableDocSearchResult;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SearchResult dialog = new SearchResult("a", 1);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SearchResult(String searchKeyword, int searchType) {
		// Initialize MySQL connection
		DBManager m = DBManager.getInstance();
		
		setBounds(100, 100, 1000, 610);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblDocresult = new JLabel("Doc search result");
		lblDocresult.setBounds(74, 78, 853, 31);
		contentPanel.add(lblDocresult);
		
		JButton btnBorrow = new JButton("CHECK OUT");
		btnBorrow.setBounds(112, 505, 117, 29);
		contentPanel.add(btnBorrow);
		
		JButton btnReserve = new JButton("RESERVE");
		btnReserve.setBounds(460, 505, 117, 29);
		contentPanel.add(btnReserve);
		String[] columnNames = {"DOCID", "TITLE", "PDATE", "PUBLISHERID", "COPYNO", "LIBID", "POSITION", "LIBNAME", "LIBADDRESS"};
		ArrayList<ArrayList<Object>> searchResult = new ArrayList<ArrayList<Object>>();
		
		if (searchType == SEARCH_TYPE_ID) {
			searchResult = m.execQuery("SELECT * FROM `DOCUMENT` D, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`DOCID` = '" + searchKeyword + "';");
		} else if (searchType == SEARCH_TYPE_TITLE) {
			searchResult = m.execQuery("SELECT * FROM `DOCUMENT` D, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`TITLE` LIKE '%" + searchKeyword + "%';");
		} else if (searchType == SEARCH_TYPE_PUBLISHER) {
			searchResult = m.execQuery("SELECT * FROM `DOCUMENT` D, `PUBLISHER` P, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`PUBLISHERID` = `P`.`PUBLISHERID` AND `P`.`PUBNAME` LIKE '%" + searchKeyword + "%';");
		}
		
		if (searchResult == null || searchResult.size() <= 0) return;

		Object[][] array = new Object[searchResult.size()][];
		for (int i = 0; i < searchResult.size(); i++) {
		    ArrayList<Object> row = searchResult.get(i);
		    array[i] = row.toArray();
		}

		DefaultTableModel tm = new DefaultTableModel(array, columnNames);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(72, 110, 855, 327);
		contentPanel.add(scrollPane);
		
		tableDocSearchResult = new JTable();
		scrollPane.setViewportView(tableDocSearchResult);
		tableDocSearchResult.setModel(tm);

	}
}
