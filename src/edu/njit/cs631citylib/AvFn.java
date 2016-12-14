package edu.njit.cs631citylib;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AvFn extends JDialog{
	
	private final JPanel contentPanel = new JPanel();
	private JTable tableAvFn;
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
	 * @throws ParseException 
	 */
	public AvFn() throws ParseException {
		setTitle("City Library");
		
		DBManager m = DBManager.getInstance();
		
		String[] columnNames = {"ReaderId","AVERAGE Fine(cents)"};
		ArrayList<ArrayList<Object>> result1 = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> row = new ArrayList<Object>();
		result1 = m.execQuery("SELECT DISTINCT READERID FROM BORROWS ;");

		//Object[][] arrayr = new Object[result1.size()][];
		Object[][] array = new Object[result1.size()][2];
		for (int i = 0; i < result1.size(); i++) {
			row.add(result1.get(i));
			array[i][0] = String.valueOf(result1.get(i));
		}
		
		ArrayList<ArrayList<Object>> result2 = new ArrayList<ArrayList<Object>>();
		//Object[][] array = new Object[result1.size()][2];
		double avg =0;
		double avfn = 0;
		for (int i = 0; i < result1.size(); i++) {
			avg=0;
			avfn=0;
			result2 = m.execQuery("SELECT BDTIME, RDTIME FROM `BORROWS` WHERE READERID =" + Integer.parseInt(array[i][0].toString().substring(1,2)) + " ;");
			for(int j=0; j < result2.size(); j++){
			ArrayList<Object> row1 = result2.get(j);
			//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			//String s1 = String.valueOf(row1.get(0));
			//String s2 = String.valueOf(row1.get(1));
			//Date bd = simpleDateFormat.parse(s1);
			//Date rd = simpleDateFormat.parse(s2);
			Timestamp a = (Timestamp)row1.get(0);
			Timestamp b = (Timestamp)row1.get(1);
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
				avg = avg + 20*diff2;
				;
				System.out.println(result2.size());
				
			}
			else{
				row.add(0.0);
			}
			avfn = avg / result2.size();
			array[i][1] = String.valueOf(avfn);
		}
			
		}
		//if (borrowResult == null || borrowResult.size() <= 0){
			//JOptionPane.showMessageDialog(null, "No Borrowed Documents");
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
		scrollPane.setBounds(6, 51, 1308, 480);
		contentPanel.add(scrollPane);
	
		tableAvFn = new JTable();
		scrollPane.setViewportView(tableAvFn);
		tableAvFn.setModel(tm);
		
		//Label label = new Label("Borrowed Books:");
		//label.setBounds(6, 10, 187, 35);
		//contentPanel.add(label);

	}

}
