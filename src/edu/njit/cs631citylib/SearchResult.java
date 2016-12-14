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
			searchResult = m.execQuery("SELECT `D`.`DOCID`, `TITLE`, `PDATE`, `D`.`PUBLISHERID`, `COPYNO`, `C`.`LIBID`, `POSITION`, `LNAME`, `LLOCATION`  FROM `DOCUMENT` D, `PUBLISHER` P, `COPY` C, `BRANCH` B WHERE `D`.`DOCID`=`C`.`DOCID` AND `C`.`LIBID`=`B`.`LIBID` AND `D`.`PUBLISHERID` = `P`.`PUBLISHERID` AND `D`.`DOCID` = '" + searchKeyword + "';");
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
				
				if (rowIndex == -1 ) {
					JOptionPane.showMessageDialog(null, "Please choose one row!");
					return;
				}
				
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
						
						// Select this reader reserved doc and remove those expired. 
						ArrayList<ArrayList<Object>> readerReserved = new ArrayList<ArrayList<Object>>();
						readerReserved = m.execQuery("SELECT * FROM RESERVES WHERE `READERID` = '" + readerId + "';");
						Object[][] readerReservedArr = new Object[readerReserved.size()][];
						for (int i = 0; i < readerReserved.size(); i++) {
						    ArrayList<Object> row = readerReserved.get(i);
						    readerReservedArr[i] = row.toArray();
						}
						Calendar cl1 = Calendar.getInstance(TimeZone.getDefault());
						int year1 = cl1.get(Calendar.YEAR);
						int month1 = cl1.get(Calendar.MONTH);
						int day1 = cl1.get(Calendar.DAY_OF_MONTH);
						cl1.set(year1, month1, day1, 18, 0, 0);  // Today's 6pm
						for (int q = 0; q < readerReservedArr.length; q++) {
							long dtime = ((Timestamp)readerReservedArr[q][5]).getTime() + 86400000;  // Reserved time + 1 day
							// If reserved after yesterday 6pm. Then check if now is after 6pm.
							if (cl1.getTimeInMillis() - dtime < 0) {
								// If now is after 6pm, Check if reserved after today 6pm.
								if ((System.currentTimeMillis() - cl1.getTimeInMillis()) > 0) {
									// If now is after 6m and reserved before today 6pm. It is expired.
									if ((dtime - 86400000) - cl1.getTimeInMillis() < 0) {
										int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
										if (deletedRows != 1) {
											JOptionPane.showMessageDialog(null, "Failed to remove reservation");
											return;
										}
									} // else if reserved after today 6pm, it isn't expired. don't remove. move to next tuple.
								}// If now is before 6pm, it isn't expired. don't remove. move to next tuple.
							} else {
							// If reserved before yesterday 6pm, then it is expired, should remove.
								int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
								if (deletedRows != 1) {
									JOptionPane.showMessageDialog(null, "Failed to remove reservation");
									return;
								}
							}		
						}		
						// Count those reserved by this reader and not expired.
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
						cl.set(year, month, day, 18, 0, 0);  // Today 6pm
						
						// Check if reserved after yesterday 6pm, then check if now is after 6pm.
						if (cl.getTimeInMillis() - ts < 0) {		
							// If now is after 6PM, then check if reserved after today 6pm
							if ((System.currentTimeMillis() - cl.getTimeInMillis()) > 0) {	
								// If reserved before today 6pm then check if more than 10
								if ((ts - 86400000) - cl.getTimeInMillis() < 0) {
									ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
									ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();
									
									// Select this reader reserved doc and remove those expired. 
									ArrayList<ArrayList<Object>> readerReserved = new ArrayList<ArrayList<Object>>();
									readerReserved = m.execQuery("SELECT * FROM RESERVES WHERE `READERID` = '" + readerId + "';");
									Object[][] readerReservedArr = new Object[readerReserved.size()][];
									for (int i = 0; i < readerReserved.size(); i++) {
									    ArrayList<Object> row = readerReserved.get(i);
									    readerReservedArr[i] = row.toArray();
									}
									Calendar cl2 = Calendar.getInstance(TimeZone.getDefault());
									int year2 = cl2.get(Calendar.YEAR);
									int month2 = cl2.get(Calendar.MONTH);
									int day2 = cl2.get(Calendar.DAY_OF_MONTH);
									cl2.set(year2, month2, day2, 18, 0, 0);
									for (int q = 0; q < readerReservedArr.length; q++) {
										long dtime = ((Timestamp)readerReservedArr[q][5]).getTime() + 86400000;  // Reserved time + 1 day
										// If reserved after yesterday 6pm. Then check if now is after 6pm.
										if (cl2.getTimeInMillis() - dtime < 0) {
											// If now is after 6pm, check if reserved after today 6pm or before.
											if ((System.currentTimeMillis() - cl2.getTimeInMillis()) > 0) {
												// If now is after 6pm and reserved before today 6pm. It is expired.
												if ((dtime - 86400000) - cl2.getTimeInMillis() < 0) {
													int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
													if (deletedRows != 1) {
														JOptionPane.showMessageDialog(null, "Failed to remove reservation");
														return;
													}
												} // else if reserved after today 6pm, it isn't expired. don't remove. move to next tuple.
											}// If now is before 6pm, don't remove. move to next tuple.
										} else {
										// If reserved before yesterday 6pm, then it is expired, should remove.
											int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
											if (deletedRows != 1) {
												JOptionPane.showMessageDialog(null, "Failed to remove reservation");
												return;
											}
										}		
									}		
									// Count those reserved by this reader and not expired.
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
								// If reserved after today 6pm cannot borrow
									JOptionPane.showMessageDialog(null, "Someone reserved it!"); 
								}
								
							} else {
							// If now is before 6pm, then can not reserve.
								JOptionPane.showMessageDialog(null, "Someone reserved it!"); 
							}
						} else {
						// If it's reserved before yesterday 6pm, then check if more than 10.
							ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
							ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();	
							
							// Select this reader reserved doc and remove those expired. 
							ArrayList<ArrayList<Object>> readerReserved = new ArrayList<ArrayList<Object>>();
							readerReserved = m.execQuery("SELECT * FROM RESERVES WHERE `READERID` = '" + readerId + "';");
							Object[][] readerReservedArr = new Object[readerReserved.size()][];
							for (int i = 0; i < readerReserved.size(); i++) {
							    ArrayList<Object> row = readerReserved.get(i);
							    readerReservedArr[i] = row.toArray();
							}
							Calendar cl3 = Calendar.getInstance(TimeZone.getDefault());
							int year3 = cl3.get(Calendar.YEAR);
							int month3 = cl3.get(Calendar.MONTH);
							int day3 = cl3.get(Calendar.DAY_OF_MONTH);
							cl3.set(year3, month3, day3, 18, 0, 0);
							for (int q = 0; q < readerReservedArr.length; q++) {
								long dtime = ((Timestamp)readerReservedArr[q][5]).getTime() + 86400000;  // Reserved time + 1 day
								// If reserved after yesterday 6pm. Then check if now is after 6pm.
								if (cl3.getTimeInMillis() - dtime < 0) {
									// If now is after 6pm, check if reserved after today's 6pm
									if ((System.currentTimeMillis() - cl3.getTimeInMillis()) > 0) {
										// If now is after 6m and reserved before today 6pm. It is expired.
										if ((dtime - 86400000) - cl3.getTimeInMillis() < 0) {
											int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
											if (deletedRows != 1) {
												JOptionPane.showMessageDialog(null, "Failed to remove reservation");
												return;
											}
										} // else if reserved after today 6pm, it isn't expired. don't remove. move to next tuple.
									}// If now is before 6pm, don't remove. move to next tuple.
								} else {
								// If reserved before yesterday 6pm, then it is expired, should remove.
									int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
									if (deletedRows != 1) {
										JOptionPane.showMessageDialog(null, "Failed to remove reservation");
										return;
									}
								}		
							}		
							// Count those reserved by this reader and not expired.
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
				
				if (rowIndex == -1 ) {
					JOptionPane.showMessageDialog(null, "Please choose one row!");
					return;
				}
				
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
						
						// Select this reader reserved doc and remove those expired. 
						ArrayList<ArrayList<Object>> readerReserved = new ArrayList<ArrayList<Object>>();
						readerReserved = m.execQuery("SELECT * FROM RESERVES WHERE `READERID` = '" + readerId + "';");
						Object[][] readerReservedArr = new Object[readerReserved.size()][];
						for (int i = 0; i < readerReserved.size(); i++) {
						    ArrayList<Object> row = readerReserved.get(i);
						    readerReservedArr[i] = row.toArray();
						}
						Calendar cl1 = Calendar.getInstance(TimeZone.getDefault());
						int year1 = cl1.get(Calendar.YEAR);
						int month1 = cl1.get(Calendar.MONTH);
						int day1 = cl1.get(Calendar.DAY_OF_MONTH);
						cl1.set(year1, month1, day1, 18, 0, 0);
						for (int q = 0; q < readerReservedArr.length; q++) {
							long dtime = ((Timestamp)readerReservedArr[q][5]).getTime() + 86400000;  // Reserved time + 1 day
							// If reserved after yesterday 6pm. Then check if now is after 6pm.
							if (cl1.getTimeInMillis() - dtime < 0) {
								// If now is after 6pm, check if reserved after today 6pm.
								if ((System.currentTimeMillis() - cl1.getTimeInMillis()) > 0) {
									// If now is after 6m and reserved before today 6pm. It is expired.
									if ((dtime - 86400000) - cl1.getTimeInMillis() < 0) {
										int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
										if (deletedRows != 1) {
											JOptionPane.showMessageDialog(null, "Failed to remove reservation");
											return;
										}
									} // else if reserved after today 6pm, it isn't expired. don't remove. move to next tuple.
								}// If now is before 6pm, don't remove. move to next tuple.
							} else {
							// If reserved before yesterday 6pm, then it is expired, should remove.
								int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
								if (deletedRows != 1) {
									JOptionPane.showMessageDialog(null, "Failed to remove reservation");
									return;
								}
							}		
						}		
						// Count those reserved by this reader and not expired.
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
							// If now is after 6PM, then check if reserved before today 6pm
							if ((System.currentTimeMillis() - cl.getTimeInMillis()) > 0) {
								
								// If now is after 6m and reserved before today 6pm. It is expired. check if more than 10.
								if ((ts - 86400000) - cl.getTimeInMillis() < 0) {
									ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
									ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();

									// Select this reader reserved doc and remove those expired. 
									ArrayList<ArrayList<Object>> readerReserved = new ArrayList<ArrayList<Object>>();
									readerReserved = m.execQuery("SELECT * FROM RESERVES WHERE `READERID` = '" + readerId + "';");
									Object[][] readerReservedArr = new Object[readerReserved.size()][];
									for (int i = 0; i < readerReserved.size(); i++) {
									    ArrayList<Object> row = readerReserved.get(i);
									    readerReservedArr[i] = row.toArray();
									}
									Calendar cl2 = Calendar.getInstance(TimeZone.getDefault());
									int year2 = cl2.get(Calendar.YEAR);
									int month2 = cl2.get(Calendar.MONTH);
									int day2 = cl2.get(Calendar.DAY_OF_MONTH);
									cl2.set(year2, month2, day2, 18, 0, 0);
									for (int q = 0; q < readerReservedArr.length; q++) {
										long dtime = ((Timestamp)readerReservedArr[q][5]).getTime() + 86400000;  // Reserved time + 1 day
										// If reserved after yesterday 6pm. Then check if now is after 6pm.
										if (cl2.getTimeInMillis() - dtime < 0) {
											// If now is after 6pm, that means this doc is expired. Should remove.
											if ((System.currentTimeMillis() - cl2.getTimeInMillis()) > 0) {
												// If now is after 6m and reserved before today 6pm. It is expired.
												if ((dtime - 86400000) - cl2.getTimeInMillis() < 0) {
													int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
													if (deletedRows != 1) {
														JOptionPane.showMessageDialog(null, "Failed to remove reservation");
														return;
													}
												} // else if reserved after today 6pm, it isn't expired. don't remove. move to next tuple.
											}// If now is before 6pm, don't remove. move to next tuple.
										} else {
										// If reserved before yesterday 6pm, then it is expired, should remove.
											int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
											if (deletedRows != 1) {
												JOptionPane.showMessageDialog(null, "Failed to remove reservation");
												return;
											}
										}		
									}		
									// Count those reserved by this reader and not expired.
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
								// If now after today 6pm and reserved after today 6pm, can not borrow
									JOptionPane.showMessageDialog(null, "Someone reserved it!"); 
								}
								
							} else {
							// If reserved after yesterday 6 pm and now is before 6pm, then can not borrow.
								JOptionPane.showMessageDialog(null, "Someone reserved it!"); 
							}
						} else {
						// If it's reserved before yesterday 6pm, then check if more than 10.
							ArrayList<ArrayList<Object>> count1 = new ArrayList<ArrayList<Object>>();
							ArrayList<ArrayList<Object>> count2 = new ArrayList<ArrayList<Object>>();
							
							// Select this reader reserved doc and remove those expired. 
							ArrayList<ArrayList<Object>> readerReserved = new ArrayList<ArrayList<Object>>();
							readerReserved = m.execQuery("SELECT * FROM RESERVES WHERE `READERID` = '" + readerId + "';");
							Object[][] readerReservedArr = new Object[readerReserved.size()][];
							for (int i = 0; i < readerReserved.size(); i++) {
							    ArrayList<Object> row = readerReserved.get(i);
							    readerReservedArr[i] = row.toArray();
							}
							Calendar cl3 = Calendar.getInstance(TimeZone.getDefault());
							int year3 = cl3.get(Calendar.YEAR);
							int month3 = cl3.get(Calendar.MONTH);
							int day3 = cl3.get(Calendar.DAY_OF_MONTH);
							cl3.set(year3, month3, day3, 18, 0, 0);
							for (int q = 0; q < readerReservedArr.length; q++) {
								long dtime = ((Timestamp)readerReservedArr[q][5]).getTime() + 86400000;  // Reserved time + 1 day
								// If reserved after yesterday 6pm. Then check if now is after 6pm.
								if (cl3.getTimeInMillis() - dtime < 0) {
									// If now is after 6pm, that means this doc is expired. Should remove.
									if ((System.currentTimeMillis() - cl3.getTimeInMillis()) > 0) {
										// If now is after 6m and reserved before today 6pm. It is expired.
										if ((dtime - 86400000) - cl3.getTimeInMillis() < 0) {
											int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
											if (deletedRows != 1) {
												JOptionPane.showMessageDialog(null, "Failed to remove reservation");
												return;
											}
										} // else if reserved after today 6pm, it isn't expired. don't remove. move to next tuple.
									}// If now is before 6pm, don't remove. move to next tuple.
								} else {
								// If reserved before yesterday 6pm, then it is expired, should remove.
									int deletedRows = m.execUpdate("DELETE FROM RESERVES WHERE `DTIME` = '" + readerReservedArr[q][5] + "';");
									if (deletedRows != 1) {
										JOptionPane.showMessageDialog(null, "Failed to remove reservation");
										return;
									}
								}		
							}		
							// Count those reserved by this reader and not expired.
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
