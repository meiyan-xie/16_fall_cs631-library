package edu.njit.cs631citylib;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

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
			DBManager.getInstance().connect();
			SearchResult dialog = new SearchResult("4", SEARCH_TYPE_ID, "1");
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
		setTitle("CITY LIBRARY");
		// Initialize MySQL connection
		DBManager m = DBManager.getInstance();
		
		setBounds(100, 100, 1000, 610);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setForeground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblDocresult = new JLabel("Document search result:");
		lblDocresult.setBounds(60, 53, 853, 31);
		contentPanel.add(lblDocresult);
		
		
		String[] columnNames = {"DOCID", "TITLE", "PDATE", "PUBLISHERID", "COPYNO", "LIBID", "POSITION", "LIBNAME", "LIBADDRESS"};
		ArrayList<ArrayList<Object>> searchResult = new ArrayList<ArrayList<Object>>();
		
		if (searchType == SEARCH_TYPE_ID) {
			searchResult = m.execQuery("SELECT `D`.`DOCID`, `TITLE`, `PDATE`, `D`.`PUBLISHERID`, `COPYNO`, `C`.`LIBID`, `POSITION`, `LNAME`, `LLOCATION`  FROM `DOCUMENT` D, `PUBLISHER` P, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`PUBLISHERID` = `P`.`PUBLISHERID` AND `D`.`DOCID` = " + searchKeyword + ";");
		} else if (searchType == SEARCH_TYPE_TITLE) {
			searchResult = m.execQuery("SELECT `D`.`DOCID`, `TITLE`, `PDATE`, `D`.`PUBLISHERID`, `COPYNO`, `C`.`LIBID`, `POSITION`, `LNAME`, `LLOCATION`  FROM `DOCUMENT` D, `PUBLISHER` P, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`PUBLISHERID` = `P`.`PUBLISHERID` AND `D`.`TITLE` LIKE '%" + searchKeyword + "%';");
		} else if (searchType == SEARCH_TYPE_PUBLISHER) {
			searchResult = m.execQuery("SELECT `D`.`DOCID`, `TITLE`, `PDATE`, `D`.`PUBLISHERID`, `COPYNO`, `C`.`LIBID`, `POSITION`, `LNAME`, `LLOCATION`  FROM `DOCUMENT` D, `PUBLISHER` P, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`PUBLISHERID` = `P`.`PUBLISHERID` AND `P`.`PUBNAME` LIKE '%" + searchKeyword + "%';");
		}
		
		if (searchResult == null || searchResult.size() <= 0) return;

		Object[][] searchArray = new Object[searchResult.size()][];
		for (int i = 0; i < searchResult.size(); i++) {
		    ArrayList<Object> row = searchResult.get(i);
		    searchArray[i] = row.toArray();
		}

		DefaultTableModel tm = new DefaultTableModel(searchArray, columnNames);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(60, 96, 871, 344);
		contentPanel.add(scrollPane);
		
		tableDocSearchResult = new JTable();
		scrollPane.setViewportView(tableDocSearchResult);
		tableDocSearchResult.setModel(tm);
		
		
		JButton btnReserve = new JButton("RESERVE");
		btnReserve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = tableDocSearchResult.getSelectedRow();
				
				ArrayList<ArrayList<Object>> reservedResult = new ArrayList<ArrayList<Object>>();
				reservedResult = m.execQuery("SELECT * FROM RESERVES R WHERE `R`.`DOCID` = '" + searchArray[rowIndex][0] + "' AND `R`.`COPYNO` = '" + searchArray[rowIndex][4] + "' AND `R`.`LIBID` = '" + searchArray[rowIndex][5] +"';");
				
				// If this copy dosen't exist in RESERVES table, then check if in the BORROW table and not return (RDTIME IS NULL).
				if (reservedResult == null || reservedResult.size() <= 0) {
					ArrayList<ArrayList<Object>> borrowResult = new ArrayList<ArrayList<Object>>();
					borrowResult = m.execQuery("SELECT * FROM BORROWS B WHERE `B`.`DOCID` = '" + searchArray[rowIndex][0] + "' AND `B`.`COPYNO` = '" + searchArray[rowIndex][4] + "' AND `B`.`LIBID` = '" + searchArray[rowIndex][5] +"' AND `B`.`RDTIME` IS NULL;");
				
					// If not in the BORROW table or returned, then check whether more than 10.
					if (borrowResult == null || borrowResult.size() <= 0) {
						ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
						ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();
						count1 = m.execQuery("SELECT COUNT(*) FROM RESERVES WHERE READERID = '" + readerId + "';");
						count2 = m.execQuery("SELECT COUNT(*) FROM BORROWS WHERE READERID = '" + readerId + "' AND RDTIME IS NULL;");
						Long count = (Long)(count1.get(0).get(0)) + (Long)(count2.get(0).get(0));
						
						// If less than 10. then can reserve.
						if (count < 10) {
							int insertedRows = m.execUpdate("INSERT INTO RESERVES(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `DTIME`) VALUES('" + readerId + "','" + searchArray[rowIndex][0] + "','" + searchArray[rowIndex][4] + "','" + searchArray[rowIndex][5] +"', NOW());");
							if (insertedRows != 1) {
								JOptionPane.showMessageDialog(null, "Reserve failed!");
							} else {
								JOptionPane.showMessageDialog(null, "Reserve seccess!");
							}
						} else {
						// If more than 10, cannot reserve.
							JOptionPane.showMessageDialog(null, "Cannot reserve more than 10 doc!");
						}	
					} else {
					// If in BORROW table and not return, then can not reserve
						JOptionPane.showMessageDialog(null, "Already been borrowed!");
					}
				} else {
				// If exist in RESERVES table, should be one tuple.
					Object[][] reserveArr = new Object[reservedResult.size()][];
					for (int i = 0; i < reservedResult.size(); i++) {
					    ArrayList<Object> row = reservedResult.get(i);
					    reserveArr[i] = row.toArray();
					}
					
					// Check if is himself, then delete the tuple in RESERVE and insert one into RESERVE
					if (((Long)reserveArr[0][1]).toString().equals(readerId)) {
						int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DOCID` = '" + searchArray[rowIndex][0] + "' AND `COPYNO` = '" + searchArray[rowIndex][4] + "' AND `LIBID` = '" + searchArray[rowIndex][5] +"';");
						if (deletedRows != 1) {
							JOptionPane.showMessageDialog(null, "Failed to remove reservation");
							return;
						}

						int insertedRows = m.execUpdate("INSERT INTO RESERVES(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `DTIME`) VALUES('" + readerId + "','" + searchArray[rowIndex][0] + "','" + searchArray[rowIndex][4] + "','" + searchArray[rowIndex][5] +"', NOW());");
						if (insertedRows != 1) {
							JOptionPane.showMessageDialog(null, "Reserve failed!");
						} else {
							JOptionPane.showMessageDialog(null, "Reserve seccess!");
						}
					} else {
					// If not himself reserved this copy, then check the if it is expired.
						long ts = ((Timestamp) reserveArr[0][5]).getTime() + 86400000; // Get the reserve time + 1 day.

						// Get today's 18pm
						Calendar cl = Calendar.getInstance(TimeZone.getDefault());
						int year = cl.get(Calendar.YEAR);
						int month = cl.get(Calendar.MONTH);
						int day = cl.get(Calendar.DAY_OF_MONTH);
						cl.set(year, month, day, 18, 0, 0);
						
						// Check if reserved after yesterday 6pm, then check if now is after 6pm.
						if (cl.getTimeInMillis() - ts < 0) {		
							// If now is after 6PM, then check if more than 10.
							if ((System.currentTimeMillis() - cl.getTimeInMillis()) > 0) {	
								
								ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
								ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();
								count1 = m.execQuery("SELECT COUNT(*) FROM RESERVES WHERE READERID = '" + readerId + "';");
								count2 = m.execQuery("SELECT COUNT(*) FROM BORROWS WHERE READERID = '" + readerId + "' AND RDTIME IS NULL;");
								Long count = (Long)(count1.get(0).get(0)) + (Long)(count2.get(0).get(0));
								
								// If less than 10. then can borrow. delete from RESERVE and insert into RESERVE.
								if (count < 10) {
									
									int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DOCID` = '" + searchArray[rowIndex][0] + "' AND `COPYNO` = '" + searchArray[rowIndex][4] + "' AND `LIBID` = '" + searchArray[rowIndex][5] +"';");
									if (deletedRows != 1) {
										JOptionPane.showMessageDialog(null, "Failed to remove reservation");
										return;
									}
									
									int insertedRows = m.execUpdate("INSERT INTO RESERVES(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `DTIME`) VALUES('" + readerId + "','" + searchArray[rowIndex][0] + "','" + searchArray[rowIndex][4] + "','" + searchArray[rowIndex][5] +"', NOW());");
									if (insertedRows != 1) {
										JOptionPane.showMessageDialog(null, "Reserve failed!");
									} else {
										JOptionPane.showMessageDialog(null, "Reserve seccess!");
									}
								} else {
								// If more than 10, cannot reserve.
									JOptionPane.showMessageDialog(null, "Cannot reserve more than 10 doc!");
								}	
								
							} else {
							// If now is before 6pm, then can not reserve.
								JOptionPane.showMessageDialog(null, "Someone reserved it!"); 
							}
						} else {
						// If it's reserved before yesterday 6pm, then check if more than 10.
							ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
							ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();
							count1 = m.execQuery("SELECT COUNT(*) FROM RESERVES WHERE READERID = '" + readerId + "';");
							count2 = m.execQuery("SELECT COUNT(*) FROM BORROWS WHERE READERID = '" + readerId + "' AND RDTIME IS NULL;");
							Long count = (Long)(count1.get(0).get(0)) + (Long)(count2.get(0).get(0));
							
							// If less than 10. then can borrow. delete from RESERVE and insert into RESERVE.
							if (count < 10) {
								
								int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DOCID` = '" + searchArray[rowIndex][0] + "' AND `COPYNO` = '" + searchArray[rowIndex][4] + "' AND `LIBID` = '" + searchArray[rowIndex][5] +"';");
								if (deletedRows != 1) {
									JOptionPane.showMessageDialog(null, "Failed to remove reservation");
									return;
								}
								
								int insertedRows = m.execUpdate("INSERT INTO RESERVES(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `DTIME`) VALUES('" + readerId + "','" + searchArray[rowIndex][0] + "','" + searchArray[rowIndex][4] + "','" + searchArray[rowIndex][5] +"', NOW());");
								if (insertedRows != 1) {
									JOptionPane.showMessageDialog(null, "Reserve failed!");
								} else {
									JOptionPane.showMessageDialog(null, "Reserve seccess!");
								}
							} else {
							// If more than 10, cannot borrow.
								JOptionPane.showMessageDialog(null, "Cannot reserve more than 10 doc!");
							}
						}
					}
				}			
			}
		});
		btnReserve.setBounds(814, 467, 117, 29);
		contentPanel.add(btnReserve);
		
		
		JButton btnBorrow = new JButton("CHECK OUT");
		btnBorrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = tableDocSearchResult.getSelectedRow();
				
				ArrayList<ArrayList<Object>> reservedResult = new ArrayList<ArrayList<Object>>();
				reservedResult = m.execQuery("SELECT * FROM RESERVES R WHERE `R`.`DOCID` = '" + searchArray[rowIndex][0] + "' AND `R`.`COPYNO` = '" + searchArray[rowIndex][4] + "' AND `R`.`LIBID` = '" + searchArray[rowIndex][5] +"';");
				
				// If this copy dosen't exist in RESERVES table, then check if in the BORROW table and not return (RDTIME IS NULL).
				if (reservedResult == null || reservedResult.size() <= 0) {
					ArrayList<ArrayList<Object>> borrowResult = new ArrayList<ArrayList<Object>>();
					borrowResult = m.execQuery("SELECT * FROM BORROWS B WHERE `B`.`DOCID` = '" + searchArray[rowIndex][0] + "' AND `B`.`COPYNO` = '" + searchArray[rowIndex][4] + "' AND `B`.`LIBID` = '" + searchArray[rowIndex][5] +"' AND `B`.`RDTIME` IS NULL;");
				
					// If not in the BORROW table or returned, then check whether more than 10.
					if (borrowResult == null || borrowResult.size() <= 0) {
						ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
						ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();
						count1 = m.execQuery("SELECT COUNT(*) FROM RESERVES WHERE READERID = '" + readerId + "';");
						count2 = m.execQuery("SELECT COUNT(*) FROM BORROWS WHERE READERID = '" + readerId + "' AND RDTIME IS NULL;");
						Long count = (Long)(count1.get(0).get(0)) + (Long)(count2.get(0).get(0));
						
						// If less than 10. then can borrow.
						if (count < 10) {
							int insertedRows = m.execUpdate("INSERT INTO BORROWS(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME`) VALUES('" + readerId + "','" + searchArray[rowIndex][0] + "','" + searchArray[rowIndex][4] + "','" + searchArray[rowIndex][5] +"', NOW(), NULL);");
							if (insertedRows != 1) {
								JOptionPane.showMessageDialog(null, "Borrow failed!");
							} else {
								JOptionPane.showMessageDialog(null, "Borrow seccess!");
							}
						} else {
						// If more than 10, cannot borrow.
							JOptionPane.showMessageDialog(null, "Cannot borrrow more than 10 doc!");
						}	
					} else {
					// If in BORROW table and not return, then can not borrow
						JOptionPane.showMessageDialog(null, "Already been borrowed!");
					}
				} else {
				// If exist in RESERVES table, should be one tuple.
					Object[][] reserveArr = new Object[reservedResult.size()][];
					for (int i = 0; i < reservedResult.size(); i++) {
					    ArrayList<Object> row = reservedResult.get(i);
					    reserveArr[i] = row.toArray();
					}
					
					// Check if is himself, then delete the tuple in RESERVE and insert one into BORROW
					if (((Long)reserveArr[0][1]).toString().equals(readerId)) {
						int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DOCID` = '" + searchArray[rowIndex][0] + "' AND `COPYNO` = '" + searchArray[rowIndex][4] + "' AND `LIBID` = '" + searchArray[rowIndex][5] +"';");
						if (deletedRows != 1) {
							JOptionPane.showMessageDialog(null, "Failed to remove reservation");
							return;
						}

						int insertedRows = m.execUpdate("INSERT INTO BORROWS(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME`) VALUES('" + readerId + "','" + searchArray[rowIndex][0] + "','" + searchArray[rowIndex][4] + "','" + searchArray[rowIndex][5] +"', NOW(), NULL);");
						if (insertedRows != 1) {
							JOptionPane.showMessageDialog(null, "Borrow failed!");
						} else {
							JOptionPane.showMessageDialog(null, "Borrow seccess!");
						}
					} else {
					// If not himself reserved this copy, then check the if it is expired.
						long ts = ((Timestamp) reserveArr[0][5]).getTime() + 86400000; // Get the reserve time + 1 day.

						// Get today's 18pm
						Calendar cl = Calendar.getInstance(TimeZone.getDefault());
						int year = cl.get(Calendar.YEAR);
						int month = cl.get(Calendar.MONTH);
						int day = cl.get(Calendar.DAY_OF_MONTH);
						cl.set(year, month, day, 18, 0, 0);
						
						// Check if reserved after yesterday 6pm, then check if now is after 6pm.
						if (cl.getTimeInMillis() - ts < 0) {		
							// If now is after 6PM, then check if more than 10.
							if ((System.currentTimeMillis() - cl.getTimeInMillis()) > 0) {	
								
								ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
								ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();
								count1 = m.execQuery("SELECT COUNT(*) FROM RESERVES WHERE READERID = '" + readerId + "';");
								count2 = m.execQuery("SELECT COUNT(*) FROM BORROWS WHERE READERID = '" + readerId + "' AND RDTIME IS NULL;");
								Long count = (Long)(count1.get(0).get(0)) + (Long)(count2.get(0).get(0));
								
								// If less than 10. then can borrow. delete from RESERVE and insert into BORROW.
								if (count < 10) {
									
									int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DOCID` = '" + searchArray[rowIndex][0] + "' AND `COPYNO` = '" + searchArray[rowIndex][4] + "' AND `LIBID` = '" + searchArray[rowIndex][5] +"';");
									if (deletedRows != 1) {
										JOptionPane.showMessageDialog(null, "Failed to remove reservation");
										return;
									}
									
									int insertedRows = m.execUpdate("INSERT INTO BORROWS(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME`) VALUES('" + readerId + "','" + searchArray[rowIndex][0] + "','" + searchArray[rowIndex][4] + "','" + searchArray[rowIndex][5] +"', NOW(), NULL);");
									if (insertedRows != 1) {
										JOptionPane.showMessageDialog(null, "Borrow failed!");
									} else {
										JOptionPane.showMessageDialog(null, "Borrow seccess!");
									}
								} else {
								// If more than 10, cannot borrow.
									JOptionPane.showMessageDialog(null, "Cannot borrrow more than 10 doc!");
								}	
								
							} else {
							// If now is before 6pm, then can not borrow.
								JOptionPane.showMessageDialog(null, "Someone reserved it!"); 
							}
						} else {
						// If it's reserved before yesterday 6pm, then check if more than 10.
							ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
							ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();
							count1 = m.execQuery("SELECT COUNT(*) FROM RESERVES WHERE READERID = '" + readerId + "';");
							count2 = m.execQuery("SELECT COUNT(*) FROM BORROWS WHERE READERID = '" + readerId + "' AND RDTIME IS NULL;");
							Long count = (Long)(count1.get(0).get(0)) + (Long)(count2.get(0).get(0));
							
							// If less than 10. then can borrow. delete from RESERVE and insert into BORROW.
							if (count < 10) {
								
								int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DOCID` = '" + searchArray[rowIndex][0] + "' AND `COPYNO` = '" + searchArray[rowIndex][4] + "' AND `LIBID` = '" + searchArray[rowIndex][5] +"';");
								if (deletedRows != 1) {
									JOptionPane.showMessageDialog(null, "Failed to remove reservation");
									return;
								}
								
								int insertedRows = m.execUpdate("INSERT INTO BORROWS(`READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME`) VALUES('" + readerId + "','" + searchArray[rowIndex][0] + "','" + searchArray[rowIndex][4] + "','" + searchArray[rowIndex][5] +"', NOW(), NULL);");
								if (insertedRows != 1) {
									JOptionPane.showMessageDialog(null, "Borrow failed!");
								} else {
									JOptionPane.showMessageDialog(null, "Borrow seccess!");
								}
							} else {
							// If more than 10, cannot borrow.
								JOptionPane.showMessageDialog(null, "Cannot borrrow more than 10 doc!");
							}
						}
					}
				}			
			}
		});
		btnBorrow.setBounds(637, 467, 117, 29);
		contentPanel.add(btnBorrow);

	}
	
}
