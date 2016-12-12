package edu.njit.cs631citylib;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class AddDocument extends JDialog{
	
	public static void main(String[] args) {
		try {
			AddDocument dialog = new AddDocument();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public AddDocument() {
		
		
        
		
        getContentPane().setBackground(Color.WHITE);
		//Connect to Database
		DBManager m = DBManager.getInstance();
		m.connect();
	
		setBounds(100, 100, 1000, 550);
		getContentPane().setLayout(null);
		
		JLabel lblAddReader = new JLabel("Add Document to Database");
		lblAddReader.setBounds(38, 14, 100, 31);
		getContentPane().add(lblAddReader);
		
		JButton btnAddBook = new JButton("Add Book");
		btnAddBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddBook dialog = new AddBook();
				dialog.setModal(true);
				dialog.setVisible(true);
				}
		});
		btnAddBook.setBounds(38, 64, 100, 29);
		getContentPane().add(btnAddBook);
			
	JButton btnAddJournal = new JButton("Add Journal");
	btnAddJournal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AddJournal dialog = new AddJournal();
					dialog.setModal(true);
					dialog.setVisible(true);
					}	
    });	
	btnAddJournal.setBounds(38, 114, 100, 29);
	getContentPane().add(btnAddJournal);
    
    JButton btnAddProc = new JButton("Add Proceedings");
    btnAddProc.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AddProc dialog = new AddProc();
					dialog.setModal(true);
					dialog.setVisible(true);
					}	
    });	
    btnAddProc.setBounds(38, 164, 100, 29);
	getContentPane().add(btnAddProc);
		
}


}
