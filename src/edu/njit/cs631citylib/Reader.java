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
	public Reader() {
		getContentPane().setBackground(Color.WHITE);
		//Connect to Database
		DBManager m = DBManager.getInstance();
	
		setBounds(100, 100, 539, 469);
		getContentPane().setLayout(null);
		
		txtDocSearch = new JTextField();
		txtDocSearch.setBounds(28, 47, 355, 26);
		getContentPane().add(txtDocSearch);
		txtDocSearch.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("City Library");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(184, 20, 81, 26);
		getContentPane().add(lblNewLabel);
		
		JLabel lblReadId = new JLabel("READ ID");
		lblReadId.setBounds(16, 6, 61, 16);
		getContentPane().add(lblReadId);
		
		//Radio Button
		JRadioButton radioButtonDocId = new JRadioButton("Decoument Id ");
		radioButtonDocId.setSelected(true);
		radioButtonDocId.setBounds(28, 75, 124, 23);
		getContentPane().add(radioButtonDocId);
		
		radioButtonTitle = new JRadioButton("Title");
		radioButtonTitle.setBounds(148, 75, 68, 23);
		getContentPane().add(radioButtonTitle);
		
		radioButtonPublisher = new JRadioButton("Publisher");
		radioButtonPublisher.setBounds(209, 75, 95, 23);
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
					JOptionPane.showMessageDialog(null, "Invaid");
					return;
				}
				
				
				ArrayList<ArrayList<Object>> result = m.execQuery("SELECT `id`, `title` FROM `DOCUMENT` WHERE `id` = " + txtDocSearch.getText() + ";");
				if (result == null || result.size() != 1) {
					JOptionPane.showMessageDialog(null, "Invaid Value");
				} else {
					System.out.println("valid");
					SearchResult dialog = new SearchResult();
					dialog.searchKeyword = txtDocSearch.getText();
					if (radioButtonTitle.isSelected()) {
						dialog.searchType = dialog.SEARCH_TYPE_TITLE;
					} else if (radioButtonPublisher.isSelected()) {
						dialog.searchType = dialog.SEARCH_TYPE_PUBLISHER ;
						
					} else {
						dialog.searchType = dialog.SEARCH_TYPE_ID;
					}
					dialog.setModal(true);
					dialog.setVisible(true);
				}
				
			}
		}
	);
		btnSearch.setBounds(395, 47, 117, 29);
		getContentPane().add(btnSearch);
		
	}
}
