package edu.njit.cs631citylib;

import java.awt.*;
import java.awt.event.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JSpinner;

public class Reader extends JDialog {

	private JTextField txtDocSearch;
	private JRadioButton radioButtonTitle;
	private JRadioButton radioButtonPublisher;
	private JLabel stausLabel;
	
	/**
	 * Create the frame.
	 */
	public Reader(String cardNumber) {
			
		getContentPane().setBackground(Color.WHITE);
		//Connect to Database
		DBManager m = DBManager.getInstance();
		m.connect();
	
		setBounds(100, 100, 572, 329);
		getContentPane().setLayout(null);
		
		txtDocSearch = new JTextField();
		txtDocSearch.setBounds(28, 91, 379, 26);
		getContentPane().add(txtDocSearch);
		txtDocSearch.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("City Library");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(226, 30, 95, 50);
		getContentPane().add(lblNewLabel);
		
		JLabel lblReadId = new JLabel("READ ID: '"+cardNumber+"'");
		lblReadId.setBounds(16, 6, 136, 16);
		getContentPane().add(lblReadId);
		
		//Radio Button
		JRadioButton radioButtonDocId = new JRadioButton("Decoument Id");
		radioButtonDocId.setSelected(true);
		radioButtonDocId.setBounds(39, 129, 124, 23);
		getContentPane().add(radioButtonDocId);
		
		radioButtonTitle = new JRadioButton("Title");
		radioButtonTitle.setBounds(195, 129, 68, 23);
		getContentPane().add(radioButtonTitle);
		
		radioButtonPublisher = new JRadioButton("Publisher");
		radioButtonPublisher.setBounds(287, 129, 95, 23);
		getContentPane().add(radioButtonPublisher);
		
		ButtonGroup bG = new ButtonGroup();
		bG.add(radioButtonDocId);
		bG.add(radioButtonTitle);
		bG.add(radioButtonPublisher);
				
		//Search Button
		JButton btnSearch = new JButton("Search");
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

		btnSearch.setBounds(425, 91, 117, 29);
		getContentPane().add(btnSearch);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(46, 197, 1, 12);
		getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 197, 536, -60);
		getContentPane().add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 223, 572, 16);
		getContentPane().add(separator_2);
		
		JLabel lblReaderProfile = new JLabel("Reader Profile");
		lblReaderProfile.setBounds(28, 251, 86, 16);
		getContentPane().add(lblReaderProfile);
		
		JButton btnNewButtonBorrow = new JButton("Borrowed Books");
		btnNewButtonBorrow.setBounds(141, 246, 136, 29);
		getContentPane().add(btnNewButtonBorrow);
		
		JButton btnNewButtonReserve = new JButton("Reserved Books");
		btnNewButtonReserve.setBounds(296, 246, 130, 29);
		getContentPane().add(btnNewButtonReserve);
		
		JButton btnNewButtonReturn = new JButton("Return");
		btnNewButtonReturn.setBounds(438, 246, 114, 29);
		getContentPane().add(btnNewButtonReturn);
		
		btnNewButtonReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Return dialog = new Return(cardNumber);
				dialog.setModal(true);
				dialog.setVisible(true);
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
		});
		
	}
}
