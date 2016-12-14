package edu.njit.cs631citylib;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;


public class Admin extends JDialog{
	private JButton BtnAddReader;
	private JButton BtnAddDocument;
	private JButton BtnSearchDoc;
	private JButton BtnGenerateReport;
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
		
		
		/*JRadioButton radioButtonAddReader = new JRadioButton("Add Reader");
		//radioButtonAddReader.setSelected(true);
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
		bG.add(radioButtonGenerateReport);*/
		
		JButton btnAddReader = new JButton("Add Reader");
		btnAddReader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddReader dialog = new AddReader();
				dialog.setModal(true);
				dialog.setVisible(true);
				}
		});
		btnAddReader.setBounds(38, 64, 150, 29);
		getContentPane().add(btnAddReader);
			
	JButton btnAddDocument = new JButton("Add Document");
    btnAddDocument.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AddDocument dialog = new AddDocument();
					dialog.setModal(true);
					dialog.setVisible(true);
					}	
    });	
    btnAddDocument.setBounds(200, 64, 150, 29);
	getContentPane().add(btnAddDocument);
    
    JButton btnSearchDoc = new JButton("Search Document");
    btnSearchDoc.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SearchDoc dialog = new SearchDoc();
					dialog.setModal(true);
					dialog.setVisible(true);
					}	
    });	
    btnSearchDoc.setBounds(38, 124, 150, 29);
	getContentPane().add(btnSearchDoc);
    
    JButton btnGenerateReport = new JButton("Generate Reports");
    btnGenerateReport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GenerateReport dialog = new GenerateReport();
					dialog.setModal(true);
					dialog.setVisible(true);
					}	
    });	
    btnGenerateReport.setBounds(200, 124, 150, 29);
	getContentPane().add(btnGenerateReport);
		//Search Button
		/*JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (txtDocSearch.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Enter a valid value");
					return;
				}*/

				/*int AFunction;
				if (radioButtonAddReader.isSelected()) {
					AddReader dialog = new AddReader();
					dialog.setModal(true);
					dialog.setVisible(true);
				} else if (radioButtonAddDocument.isSelected()) {
					AddDocument dialog = new AddDocument();
					dialog.setModal(true);
					dialog.setVisible(true);
				} else if (radioButtonSearchDoc.isSelected()){
					SearchDoc dialog = new SearchDoc();
					dialog.setModal(true);
					dialog.setVisible(true);
				}else if (radioButtonGenerateReport.isSelected()){
					GenerateReport dialog = new GenerateReport();
					dialog.setModal(true);
					dialog.setVisible(true);
				}
				
			/*}
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
