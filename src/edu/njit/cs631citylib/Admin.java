package edu.njit.cs631citylib;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;


public class Admin extends JDialog{
	private JRadioButton radioButtonAddReader;
	private JRadioButton radioButtonAddDocument;
	private JRadioButton radioButtonSearchDoc;
	private JRadioButton radioButtonGenerateReport;
	//private JLabel stausLabel;
	
	/**
	 * Create the frame.
	 */
	public Admin() {
			
		getContentPane().setBackground(Color.WHITE);
		//Connect to Database
		DBManager m = DBManager.getInstance();
		m.connect();
	
		setBounds(100, 100, 542, 286);
		getContentPane().setLayout(null);
		
		//txtDocSearch = new JTextField();
		//txtDocSearch.setBounds(28, 70, 355, 26);
		//getContentPane().add(txtDocSearch);
		//txtDocSearch.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("City Library");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(202, 24, 181, 50);
		getContentPane().add(lblNewLabel);
		
		JLabel lblAdminId = new JLabel("ADMIN");
		lblAdminId.setBounds(16, 6, 61, 16);
		getContentPane().add(lblAdminId);
		
		//Radio Button
		JRadioButton radioButtonAddReader = new JRadioButton("Add Reader");
		radioButtonAddReader.setSelected(true);
		radioButtonAddReader.setBounds(38, 108, 124, 23);
		getContentPane().add(radioButtonAddReader);
		
		radioButtonAddDocument = new JRadioButton("Add Document");
		radioButtonAddDocument.setBounds(175, 108, 124, 23);
		getContentPane().add(radioButtonAddDocument);
		
		radioButtonSearchDoc = new JRadioButton("Search Document");
		radioButtonSearchDoc.setBounds(38, 158, 124, 23);
		getContentPane().add(radioButtonSearchDoc);
		
		radioButtonGenerateReport = new JRadioButton("Generate Reports");
		radioButtonGenerateReport.setBounds(175, 158, 124, 23);
		getContentPane().add(radioButtonGenerateReport);
		
		ButtonGroup bG = new ButtonGroup();
		bG.add(radioButtonAddReader);
		bG.add(radioButtonAddDocument);
		bG.add(radioButtonSearchDoc);
		bG.add(radioButtonGenerateReport);
				
		//Search Button
		/*JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (txtDocSearch.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Enter a valid value");
					return;
				}

				int searchType;
				if (radioButtonTitle.isSelected()) {
					searchType = SearchResult.SEARCH_TYPE_TITLE;
				} else if (radioButtonPublisher.isSelected()) {
					searchType = SearchResult.SEARCH_TYPE_PUBLISHER ;
				} else {
					searchType = SearchResult.SEARCH_TYPE_ID;
				}
				SearchResult dialog = new SearchResult(txtDocSearch.getText(), searchType, cardNumber);
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});

		btnSearch.setBounds(395, 70, 117, 29);
		getContentPane().add(btnSearch);*/
		
		JSeparator separator = new JSeparator();
		separator.setBounds(46, 197, 1, 12);
		getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 197, 536, -60);
		getContentPane().add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 207, 542, 16);
		getContentPane().add(separator_2);
		
		/*JLabel lblReaderProfile = new JLabel("Reader Profile");
		lblReaderProfile.setBounds(28, 226, 86, 16);
		getContentPane().add(lblReaderProfile);
		
		JButton btnNewButtonBorrow = new JButton("Borrow");
		btnNewButtonBorrow.setBounds(142, 221, 87, 29);
		getContentPane().add(btnNewButtonBorrow);
		
		JButton btnNewButtonReserve = new JButton("Reserve");
		btnNewButtonReserve.setBounds(241, 221, 92, 29);
		getContentPane().add(btnNewButtonReserve);
		
		JButton btnNewButtonHistory = new JButton("History");
		btnNewButtonHistory.setBounds(345, 221, 90, 29);
		getContentPane().add(btnNewButtonHistory);
		
		btnNewButtonHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnNewButtonReserve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reserve dialog = new Reserve(cardNumber);
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
		btnNewButtonBorrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Borrow dialog = new Borrow(cardNumber);
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});*/
		
	}

}
