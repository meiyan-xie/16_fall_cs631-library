package edu.njit.cs631citylib;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;


public class AddBook extends JDialog{
	
	public static void main(String[] args) {
		try {
			AddBook dialog = new AddBook();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public AddBook() {
		
		
        
		
        getContentPane().setBackground(Color.WHITE);
		//Connect to Database
		DBManager m = DBManager.getInstance();
		m.connect();
	
		setBounds(100, 100, 500, 550);
		getContentPane().setLayout(null);
		
		JLabel lblAddBook = new JLabel("Add Book to Database");
		lblAddBook.setBounds(74, 78, 200, 31);
		getContentPane().add(lblAddBook);
		
		JLabel lblBookID = new JLabel("ID");
		lblBookID.setBounds(38, 149, 90, 16);
		getContentPane().add(lblBookID);
		
		JTextField txtBookID = new JTextField();
		txtBookID.setBounds(125, 149, 130, 26);
		getContentPane().add(txtBookID);
		txtBookID.setColumns(10);
		
		JLabel lblISBN = new JLabel("ISBN");
		lblISBN.setBounds(38, 179, 90, 16);
		getContentPane().add(lblISBN);
		
		JTextField txtISBN = new JTextField();
		txtISBN.setBounds(125, 179, 130, 26);
		getContentPane().add(txtISBN);
		txtISBN.setColumns(10);
		
		JLabel lblTitle = new JLabel("NAME");
		lblTitle.setBounds(38, 229, 90, 16);
		getContentPane().add(lblTitle);
		
		JTextField txtTitle = new JTextField();
		txtTitle.setBounds(125, 229, 130, 26);
		getContentPane().add(txtTitle);
		txtTitle.setColumns(10);
		
		JLabel lblPDate = new JLabel("PDATE");
		lblPDate.setBounds(38, 269, 90, 16);
		getContentPane().add(lblPDate);
		
		JTextField txtPDate = new JTextField();
		txtPDate.setBounds(125, 269, 130, 26);
		getContentPane().add(txtPDate);
		txtPDate.setColumns(10);
		
		JLabel lblPID = new JLabel("PID");
		lblPID.setBounds(38, 309, 90, 16);
		getContentPane().add(lblPID);
		
		JTextField txtPID = new JTextField();
		txtPID.setBounds(125, 309, 130, 26);
		getContentPane().add(txtPID);
		txtPID.setColumns(10);
		
		JLabel lblLID = new JLabel("BRANCH NO.");
		lblLID.setBounds(38, 359, 90, 16);
		getContentPane().add(lblLID);
		
		JTextField txtLID = new JTextField();
		txtLID.setBounds(125, 359, 130, 26);
		getContentPane().add(txtLID);
		txtLID.setColumns(10);
		
		JLabel lblPos = new JLabel("POSITION");
		lblPos.setBounds(38, 409, 90, 16);
		getContentPane().add(lblPos);
		
		JTextField txtPos = new JTextField();
		txtPos.setBounds(125, 409, 130, 26);
		getContentPane().add(txtPos);
		txtPos.setColumns(10);
		
		JButton btnAddBook = new JButton("Add Book");
		btnAddBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtBookID.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please type ID");
					return;
				}
				if (txtISBN.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please specify ISBN");
					return;
				}
				if (txtTitle.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please specify name");
					return;
				}
				if (txtLID.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please specify Branch");
					return;
				}
				
					
				String id = txtBookID.getText();
				Integer idi = Integer.parseInt(id);
				String lid = txtLID.getText();
				Integer idl = Integer.parseInt(lid);
				String pid = txtPID.getText();
				Integer idp = Integer.parseInt(pid);
				//String ty = txtReaderType.getText();
				//String nm = txtReaderName.getText();
				//String ad = txtReaderAdd.getText();
				ArrayList<ArrayList<Object>> result = m.execQuery("SELECT * FROM `DOCUMENT` WHERE DOCID = '" + txtBookID.getText() + "';");
				if(result.size() ==0){
			
				m.execUpdate("INSERT INTO DOCUMENT (DOCID, TITLE, PDATE, PUBLISHERID) "
				          +"VALUES ("+ idi + ",'" + txtTitle.getText() + "','" + txtPDate.getText() + "'," + idp +")");
				m.execUpdate("INSERT INTO BOOK (DOCID, ISBN) "
				          +"VALUES ("+ idi + ",'" + txtISBN.getText() + "')" );
				}
				
				
			    ArrayList<ArrayList<Object>> result1 = m.execQuery("SELECT * FROM `COPY` WHERE DOCID = '" + txtBookID.getText() + "';");
			    Integer r = result1.size() + 1;
				m.execUpdate("INSERT INTO COPY (DOCID, COPYNO, LIBID, POSITION) "
				          +"VALUES ("+ idi + "," + r + "," + idl + ",'"+ txtPos.getText() + "')");
			
		}});
		btnAddBook.setBounds(280, 179, 149, 29);
		getContentPane().add(btnAddBook);
		
}

}
