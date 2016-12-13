package edu.njit.cs631citylib;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.Timestamp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AdminSearchDoc extends JDialog{
	
	private final JPanel contentPanel = new JPanel();
	private JTable tableDocStatusResult;
	public static void main(String[] args) {
		try {
			SearchDoc dialog = new SearchDoc();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public AdminSearchDoc(String docid, String copyno, String lid){
		
		getContentPane().setBackground(Color.WHITE);
		//Connect to Database
		DBManager m = DBManager.getInstance();
		m.connect();

		setBounds(100, 100, 500, 550);
		getContentPane().setLayout(null);
		
		Integer idd = Integer.parseInt(docid);
		Integer cpn = Integer.parseInt(copyno);
		Integer idl = Integer.parseInt(lid);
		
		String[] columnNames = {"DOCID", "COPYNO", "LIBID", "STATUS"};
		String status = "NULL";
		ArrayList<ArrayList<Object>> result = m.execQuery("SELECT * FROM `COPY` WHERE DOCID = " + idd + " AND COPYNO = " + cpn + " AND LIBID = " + idl + ";");
		if(result.size()==0){
			//JLabel lblNotExists = new JLabel("The COPY DOES NOT EXISTS.PLEASE VERIFY THE INPUT");
			//lblNotExists.setBounds(38, 69, 250, 50);
			//getContentPane().add(lblNotExists);
			status = "does not exists";
		}
		else{
			
			ArrayList<ArrayList<Object>> result1 = m.execQuery("SELECT * FROM `BORROWS` WHERE DOCID = " + idd + " AND COPYNO = " + cpn + " AND LIBID = "+ idl + ";");
			ArrayList<ArrayList<Object>> result2 = m.execQuery("SELECT * FROM `RESERVES` WHERE DOCID = " + idd + " AND COPYNO = " + cpn + " AND LIBID = "+ idl + ";");
			//String rs1 = result1.toString();
			
			//Object[][] arrayr = new Object[result1.size()][];
			//for (int i = 0; i < result1.size(); i++) {
			Timestamp a = null;
			if (result1.size()!=0){
				ArrayList<Object> row = result1.get(1);
				a = (Timestamp)row.get(6);
			}
				if(a == null){
					status = "borrowed";
					//break;
				}
				else if(result2.size() !=0){
					status = "borrowed and reserved";
					//break;
				}
				else{
					status = "on shelf";
					//break;
				}
			//}
			
		}
		
		String[][] array = new String[2][4];
		array[0][0] = columnNames[0];
		array[0][1] = columnNames[1];
		array[0][2] = columnNames[2];
		array[0][3] = columnNames[3];
		
		array[1][0] = idd.toString();
		array[1][1] = cpn.toString();
		array[1][2] = idl.toString();
		array[1][3] = status;
		
        DefaultTableModel tm = new DefaultTableModel(array, columnNames);
		
		setBounds(100, 100, 1358, 610);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 1352, 582);
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 51, 1308, 480);
		contentPanel.add(scrollPane);
	
		tableDocStatusResult = new JTable();
		scrollPane.setViewportView(tableDocStatusResult);
		tableDocStatusResult.setModel(tm);
		
		
		

}
}
