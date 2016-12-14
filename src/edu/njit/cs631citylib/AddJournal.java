package edu.njit.cs631citylib;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;


public class AddJournal extends JDialog{
	
	public static void main(String[] args) {
		try {
			AddDocument dialog = new AddDocument();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public AddJournal() {
		
		
        
		
        getContentPane().setBackground(Color.WHITE);
		//Connect to Database
		DBManager m = DBManager.getInstance();
		m.connect();
	
		setBounds(100, 100, 500, 1000);
		getContentPane().setLayout(null);
		
		JLabel lblAddJounal = new JLabel("Add Journal to Database");
		lblAddJounal.setBounds(74, 78, 200, 31);
		getContentPane().add(lblAddJounal);
		
		JLabel lblJournalID = new JLabel("ID");
		lblJournalID.setBounds(38, 149, 90, 16);
		getContentPane().add(lblJournalID);
		
		JTextField txtJournalID = new JTextField();
		txtJournalID.setBounds(125, 149, 130, 26);
		getContentPane().add(txtJournalID);
		txtJournalID.setColumns(10);
		
		
		JLabel lblTitle = new JLabel("NAME");
		lblTitle.setBounds(38, 229, 90, 16);
		getContentPane().add(lblTitle);
		
		JTextField txtTitle = new JTextField();
		txtTitle.setBounds(125, 229, 130, 26);
		getContentPane().add(txtTitle);
		txtTitle.setColumns(10);
		
		JLabel lblVolume = new JLabel("VOLUME");
		lblVolume.setBounds(38, 269, 90, 16);
		getContentPane().add(lblVolume);
		
		JTextField txtVolume = new JTextField();
		txtVolume.setBounds(125, 269, 130, 26);
		getContentPane().add(txtVolume);
		txtVolume.setColumns(10);
		
		JLabel lblIssue = new JLabel("ISSUE No.");
		lblIssue.setBounds(38, 309, 90, 16);
		getContentPane().add(lblIssue);
		
		JTextField txtIssue = new JTextField();
		txtIssue.setBounds(125, 309, 130, 26);
		getContentPane().add(txtIssue);
		txtIssue.setColumns(10);
		
		JLabel lblScope = new JLabel("SCOPE");
		lblScope.setBounds(38, 369, 90, 16);
		getContentPane().add(lblScope);
		
		JTextField txtScope = new JTextField();
		txtScope.setBounds(125, 369, 130, 26);
		getContentPane().add(txtScope);
		txtScope.setColumns(10);
		
		JLabel lblPDate = new JLabel("PDATE");
		lblPDate.setBounds(38, 409, 90, 16);
		getContentPane().add(lblPDate);
		
		JTextField txtPDate = new JTextField();
		txtPDate.setBounds(125, 409, 130, 26);
		getContentPane().add(txtPDate);
		txtPDate.setColumns(10);
		
		JLabel lblPID = new JLabel("PID");
		lblPID.setBounds(38, 469, 90, 16);
		getContentPane().add(lblPID);
		
		JTextField txtPID = new JTextField();
		txtPID.setBounds(125, 469, 130, 26);
		getContentPane().add(txtPID);
		txtPID.setColumns(10);
		
		JLabel lblEID = new JLabel("EDITORID");
		lblEID.setBounds(38, 509, 90, 16);
		getContentPane().add(lblEID);
		
		JTextField txtEID = new JTextField();
		txtEID.setBounds(125, 509, 130, 26);
		getContentPane().add(txtEID);
		txtEID.setColumns(10);
		
		JLabel lblLID = new JLabel("BRANCH NO.");
		lblLID.setBounds(38, 569, 90, 16);
		getContentPane().add(lblLID);
		
		JTextField txtLID = new JTextField();
		txtLID.setBounds(125, 569, 130, 26);
		getContentPane().add(txtLID);
		txtLID.setColumns(10);
		
		JLabel lblPos = new JLabel("POSITION");
		lblPos.setBounds(38, 609, 90, 16);
		getContentPane().add(lblPos);
		
		JTextField txtPos = new JTextField();
		txtPos.setBounds(125, 609, 130, 26);
		getContentPane().add(txtPos);
		txtPos.setColumns(10);
		
		
		
		JButton btnAddJournal = new JButton("Add Journal");
		btnAddJournal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtJournalID.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please type ID");
					return;
				}
				if (txtIssue.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please specify Issue NO");
					return;
				}
				if (txtLID.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please specify Branch");
					return;
				}
				
					
				String id = txtJournalID.getText();
				Integer idi = Integer.parseInt(id);
				String lid = txtLID.getText();
				Integer idl = Integer.parseInt(lid);
				String pid = txtPID.getText();
				Integer idp = Integer.parseInt(pid);
				String eid = txtEID.getText();
				Integer ide = Integer.parseInt(eid);
				//String ty = txtReaderType.getText();
				//String nm = txtReaderName.getText();
				//String ad = txtReaderAdd.getText();
				
				ArrayList<ArrayList<Object>> resultl = m.execQuery("SELECT * FROM `BRANCH` WHERE LIBID = '" + txtLID.getText() + "';");
				if(resultl.size()==0){
					
						JOptionPane.showMessageDialog(null, "No BRANCH  WITH THIS ID. CANNOT INSERT");
					
					
				}
				ArrayList<ArrayList<Object>> resultp = m.execQuery("SELECT * FROM `PUBLISHER` WHERE PUBLISHERID = '" + txtPID.getText() + "';");
				if(resultp.size()==0){
					
						JOptionPane.showMessageDialog(null, "No PUBLISHER  WITH THIS ID. CANNOT INSERT");
					
					
				}
				ArrayList<ArrayList<Object>> resulte = m.execQuery("SELECT * FROM `CHIEF_EDITOR` WHERE EDITOR_ID = '" + txtEID.getText() + "';");
				if(resultp.size()==0){
					
						JOptionPane.showMessageDialog(null, "No EDITOR  WITH THIS ID. CANNOT INSERT");
					
					
				}
				ArrayList<ArrayList<Object>> result = m.execQuery("SELECT * FROM `DOCUMENT` WHERE DOCID = '" + txtJournalID.getText() + "';");
				if(result.size() ==0){
			
				m.execUpdate("INSERT INTO DOCUMENT (DOCID, TITLE, PDATE, PUBLISHERID) "
				          +"VALUES ("+ idi + ",'" + txtTitle.getText() + "','" + txtPDate.getText() + "'," + idp +")");
				m.execUpdate("INSERT INTO JOURNAL_VOLUME (DOCID, JVOLUME, EDITOR_ID) "
				          +"VALUES ("+ idi + ",'" + txtVolume.getText() + "'," + ide + ")" );
				m.execUpdate("INSERT INTO JOURNAL_ISSUE (DOCID, ISSUE_NO, SCOPE) "
				          +"VALUES ("+ idi + ",'" + txtIssue.getText() + "','" + txtScope.getText() + "')" );
				
				JOptionPane.showMessageDialog(null, "New journal issue inserted.");
				}
				
				
			    ArrayList<ArrayList<Object>> result1 = m.execQuery("SELECT * FROM `COPY` WHERE DOCID = '" + txtJournalID.getText() + "';");
			    Integer r = result1.size() + 1;
				m.execUpdate("INSERT INTO COPY (DOCID, COPYNO, LIBID, POSITION) "
				          +"VALUES ("+ idi + "," + r + "," + idl + ",'"+ txtPos.getText() + "')");
				JOptionPane.showMessageDialog(null, "1 copy of journal inserted into COPY Table");
			
		}});
		btnAddJournal.setBounds(280, 179, 149, 29);
		getContentPane().add(btnAddJournal);
		
}


}
