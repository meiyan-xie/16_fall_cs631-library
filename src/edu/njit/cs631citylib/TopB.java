package edu.njit.cs631citylib;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TopB extends JDialog{
	private final JPanel contentPanel = new JPanel();
	private JTable tableTopB;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DBManager m = DBManager.getInstance();
			m.connect();
			Borrow dialog = new Borrow("1");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TopB() {
		
		DBManager m = DBManager.getInstance();
		
		String[] columnNames = {"DOCID","No. of BORROWS"};
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		
		result = m.execQuery("SELECT `DOCID`, COUNT(*) FROM `BORROWS` GROUP BY DOCID ORDER BY COUNT(*) DESC LIMIT 10;");

		Object[][] array = new Object[result.size()][];
		for (int i = 0; i < result.size(); i++) {
			ArrayList<Object> row = result.get(i);
			/*Timestamp a = (Timestamp)row.get(5);
			Timestamp b = (Timestamp)row.get(6);
			long diff = 0;
			if (b == null){
				long b2 = System.currentTimeMillis();
				diff = b2 - a.getTime();
			
			}
			else{
				diff =  b.getTime() - a.getTime();
				}
			double diff1 = (double)diff / 86400000;
			if (diff1 > 20){
				diff1= diff1 - 20;
				double diff2 = Math.ceil(diff1);
				row.add(20*diff2);
			}
			else{
				row.add(0.0);
			}*/
			array[i] = row.toArray();
		}

		//if (borrowResult == null || borrowResult.size() <= 0){
			//JOptionPane.showMessageDialog(null, "No Reserved Documents");
		//}

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
	
		tableTopB = new JTable();
		scrollPane.setViewportView(tableTopB);
		tableTopB.setModel(tm);

	}

}
