package edu.njit.cs631citylib;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

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
			SearchResult dialog = new SearchResult("a", 1, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SearchResult(String searchKeyword, int searchType, String readerId) {
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
		
		
		String[] columnNames = {"DOCID", "TITLE", "PDATE", "PUBLISHERID", "COPYNO", "LIBID", "POSITION", "LIBNAME", "LIBADDRESS"};
		ArrayList<ArrayList<Object>> searchResult = new ArrayList<ArrayList<Object>>();
		
		if (searchType == SEARCH_TYPE_ID) {
			searchResult = m.execQuery("SELECT * FROM `DOCUMENT` D, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`DOCID` = " + searchKeyword + ";");
		} else if (searchType == SEARCH_TYPE_TITLE) {
			searchResult = m.execQuery("SELECT * FROM `DOCUMENT` D, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`TITLE` LIKE '%" + searchKeyword + "%';");
		} else if (searchType == SEARCH_TYPE_PUBLISHER) {
			searchResult = m.execQuery("SELECT `D`.`DOCID`, `TITLE`, `PDATE`, `D`.`PUBLISHERID`, `COPYNO`, `C`.`LIBID`, `POSITION`, `LNAME`, `LLOCATION`  FROM `DOCUMENT` D, `PUBLISHER` P, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`PUBLISHERID` = `P`.`PUBLISHERID` AND `P`.`PUBNAME` LIKE '%" + searchKeyword + "%';");
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
		
		
		JButton btnReserve = new JButton("RESERVE");
		btnReserve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = tableDocSearchResult.getSelectedRow();
				
				
				int affectedRows = m.execUpdate("INSERT INTO RESERVES(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `DTIME`) VALUES(" + readerId + "," + array[rowIndex][0] + "," + array[rowIndex][4] + "," + array[rowIndex][5] +", NOW());");
				if (affectedRows != 1) {
					JOptionPane.showMessageDialog(null, "Reserve failed!");
				}
				JOptionPane.showMessageDialog(null, "Reserve seccess!");
			}
		});
		btnReserve.setBounds(460, 505, 117, 29);
		contentPanel.add(btnReserve);
		
		
		JButton btnBorrow = new JButton("CHECK OUT");
		btnBorrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = tableDocSearchResult.getSelectedRow();
				
				ArrayList<ArrayList<Object>> reservedResult = new ArrayList<ArrayList<Object>>();
				reservedResult = m.execQuery("SELECT * FROM RESERVES R WHERE `R`.`DOCID` = '" + array[rowIndex][0] + "' AND `R`.`COPYNO` = '" + array[rowIndex][4] + "' AND `R`.`LIBID` = '" + array[rowIndex][5] +"';");
				
				// If this copy dosen't exist in RESERVES table, then check if in the BORROW table.
				if (reservedResult == null || reservedResult.size() <= 0) {
					ArrayList<ArrayList<Object>> borrowResult = new ArrayList<ArrayList<Object>>();
					borrowResult = m.execQuery("SELECT * FROM BORROWS B WHERE `B`.`DOCID` = '" + array[rowIndex][0] + "' AND `B`.`COPYNO` = '" + array[rowIndex][4] + "' AND `B`.`LIBID` = '" + array[rowIndex][5] +"';");
				
					// If not in the BORROW table, then can be borrowed, insert one tuple into BORROW table.
					if (borrowResult == null || borrowResult.size() <= 0) {
						int insertedRows = m.execUpdate("INSERT INTO BORROWS(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME`) VALUES('" + readerId + "','" + array[rowIndex][0] + "','" + array[rowIndex][4] + "','" + array[rowIndex][5] +"', NOW(), NULL);");
						if (insertedRows != 1) {
							JOptionPane.showMessageDialog(null, "Borrow failed!");
						}
						JOptionPane.showMessageDialog(null, "Borrow seccess!");
					} else {
						JOptionPane.showMessageDialog(null, "Already be borrowed!");
					}
					
				} else {
					// If exist, should be one tuple. no duplicate tuple.
					Object[][] arr = new Object[reservedResult.size()][];
					for (int i = 0; i < reservedResult.size(); i++) {
					    ArrayList<Object> row = reservedResult.get(i);
					    arr[i] = row.toArray();
					}
					
					// Check if himself, then delete the tuple in RESERVE and insert one into BORROW
					if (arr[0][1] == readerId) {
						int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DOCID` = '" + array[rowIndex][0] + "' AND `COPYNO` = '" + array[rowIndex][4] + "' AND `LIBID` = '" + array[rowIndex][5] +"';");
						if (deletedRows != 1) {
							JOptionPane.showMessageDialog(null, "Failed to remove reservation");
							return;
						}

						int insertedRows = m.execUpdate("INSERT INTO BORROWS(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME`) VALUES('" + readerId + "','" + array[rowIndex][0] + "','" + array[rowIndex][4] + "','" + array[rowIndex][5] +"', NOW(), NULL);");
						if (insertedRows != 1) {
							JOptionPane.showMessageDialog(null, "Borrow failed!");
						}
						JOptionPane.showMessageDialog(null, "Borrow seccess!");
					} else {
						// If not himself reserved this copy, then check the if is expired.
						long ts = ((Timestamp) arr[0][5]).getTime() + 86400000;
						LocalDateTime dateTime = LocalDateTime.now();
						
						@SuppressWarnings("deprecation")
						
						// Get today's 6pm time
						Timestamp sixPM = new Timestamp(dateTime.getYear(), dateTime.getMonth().getValue(), dateTime.getDayOfMonth(), 18, 0, 0, 0);
						
						// Check if reserved after yesterday 6pm, then check if now is after 6pm.
						if (sixPM.getTime() - ts < 0) {
							
							// If now is after 6PM, then can borrow.
							if ((System.currentTimeMillis() - sixPM.getTime()) > 0) {
								int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DOCID` = '" + array[rowIndex][0] + "' AND `COPYNO` = '" + array[rowIndex][4] + "' AND `LIBID` = '" + array[rowIndex][5] +"';");
								if (deletedRows != 1) {
									JOptionPane.showMessageDialog(null, "Failed to remove reservation");
									return;
								}

								int insertedRows = m.execUpdate("INSERT INTO BORROWS(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME`) VALUES('" + readerId + "','" + array[rowIndex][0] + "','" + array[rowIndex][4] + "','" + array[rowIndex][5] +"', NOW(), NULL);");
								if (insertedRows != 1) {
									JOptionPane.showMessageDialog(null, "Borrow failed!");
								}
								JOptionPane.showMessageDialog(null, "Borrow seccess!");
							} else {
								// If now is before 6pm, then can not borrow.
								JOptionPane.showMessageDialog(null, "Someone reserved it!");
							}
						} else {
							// If it's reserved before yesterday 6pm, then can borrow.
							int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DOCID` = '" + array[rowIndex][0] + "' AND `COPYNO` = '" + array[rowIndex][4] + "' AND `LIBID` = '" + array[rowIndex][5] +"';");
							if (deletedRows != 1) {
								JOptionPane.showMessageDialog(null, "Failed to remove reservation");
								return;
							}

							int insertedRows = m.execUpdate("INSERT INTO BORROWS(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME`) VALUES('" + readerId + "','" + array[rowIndex][0] + "','" + array[rowIndex][4] + "','" + array[rowIndex][5] +"', NOW(), NULL);");
							if (insertedRows != 1) {
								JOptionPane.showMessageDialog(null, "Borrow failed!");
							}
							JOptionPane.showMessageDialog(null, "Borrow seccess!");
						}
					}
					
					
					//int affectedRows = m.execUpdate("INSERT INTO BORROWS(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME`) VALUES(" + readerId + "," + array[rowIndex][0] + "," + array[rowIndex][4] + "," + array[rowIndex][5] +", NOW(), NULL);");
					//if (affectedRows != 1) {
						//JOptionPane.showMessageDialog(null, "Borrow failed!");
				//	}
					//JOptionPane.showMessageDialog(null, "Borrow seccess!");
				}			
			}
		});
		btnBorrow.setBounds(112, 505, 117, 29);
		contentPanel.add(btnBorrow);

	}
	
}
