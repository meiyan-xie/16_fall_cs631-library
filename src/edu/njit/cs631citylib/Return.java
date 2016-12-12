package edu.njit.cs631citylib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Return extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tableDocBorrowResult;
	private JButton btnReturn;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DBManager m = DBManager.getInstance();
			m.connect();
			Return dialog = new Return("1");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Return(String cardNumber) {
		DBManager m = DBManager.getInstance();
		String[] columnNames = {"BorrowNo", "ReaderId", "DocId", "CopyNo", "LibId", "BDTime"};
		ArrayList<ArrayList<Object>> borrowResult = new ArrayList<ArrayList<Object>>();
		borrowResult = m.execQuery("SELECT `BORNUMBER`, `READERID`, `DOCID`, `COPYNO`, `LIBID`, `BDTIME`, `RDTIME` FROM `BORROWS` WHERE `READERID` = " + cardNumber + ";");

		Object[][] array = new Object[borrowResult.size()][];
		int j = 0;
		for (int i = 0; i < borrowResult.size(); i++) {
			ArrayList<Object> row = borrowResult.get(i);
			Timestamp b = (Timestamp)row.get(6);
				if (b == null){
				row.remove(6);
				array[j] = row.toArray();
				j = j + 1;
				}
			else{
				continue;
				}
		}
		List <Integer>list1 = new ArrayList<Integer>();
		for (int i = 0; i < j ; i++){
			list1.add(i);
		}
		
		if (borrowResult == null || borrowResult.size() <= 0){
			JOptionPane.showMessageDialog(null, "No Borrowed Documents");
		}

		DefaultTableModel tm = new DefaultTableModel(array, columnNames);
		
		setBounds(100, 100, 1358, 610);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 1352, 582);
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(52, 37, 1245, 480);
		contentPanel.add(scrollPane);
	
		tableDocBorrowResult = new JTable();
		scrollPane.setViewportView(tableDocBorrowResult);
		tableDocBorrowResult.setModel(tm);
		
		// Return a Book		
		btnReturn = new JButton("Return");
		
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				int rowIndex = tableDocBorrowResult.getSelectedRow();
				int index = list1.get(rowIndex);
				
				long time = System.currentTimeMillis();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
				Timestamp a = (Timestamp)array[index][5];
				long diff =  time - a.getTime();
				double diff1 = (double)diff / 86400000;
				double diff2 = 0; 
				if (diff1 > 20){
					diff1= diff1 - 20;
					diff2 = Math.ceil(diff1);
					diff2 = diff2*20;
				}

								
				int affectedRows = m.execUpdate("UPDATE BORROWS SET `RDTIME`= '" +timestamp+"' WHERE `BORNUMBER` = '" + array[index][0] + "' AND `READERID` = '" + cardNumber + "';");
				
				if (diff2 == 0){
					JOptionPane.showMessageDialog(null, "Return Sucess. Books are returned on time.");}
				else {
					JOptionPane.showMessageDialog(null, "Return Sucess. You are fined '"+ diff2 +"'cents.");
				}
		        // remove selected row from the model
				tm.removeRow(rowIndex);	
				list1.remove(rowIndex);
				
				}	
		});
		btnReturn.setBounds(1180, 529, 117, 29);
		contentPanel.add(btnReturn);
	}		
		
		
}
