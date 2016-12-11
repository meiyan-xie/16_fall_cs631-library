package edu.njit.cs631citylib;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Reader extends JDialog {

	private JTextField txtDocSearch;
	private JRadioButton radioButton_1;
	private JRadioButton rdbtnPublisher;


	/**
	 * Create the frame.
	 */
	public Reader() {
		
		DBManager m = DBManager.getInstance();
		if (!m.connect()) {
			JOptionPane.showMessageDialog(null, "Could not connect to database");
			System.exit(1);
		}

		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		txtDocSearch = new JTextField();
		txtDocSearch.setBounds(28, 47, 300, 26);
		getContentPane().add(txtDocSearch);
		txtDocSearch.setColumns(10);
		
		JRadioButton radioButton = new JRadioButton("Decoument Id ");
		radioButton.setBounds(28, 75, 124, 23);
		getContentPane().add(radioButton);
		
		radioButton_1 = new JRadioButton("Title");
		radioButton_1.setBounds(148, 75, 68, 23);
		getContentPane().add(radioButton_1);
		
		rdbtnPublisher = new JRadioButton("Publisher");
		rdbtnPublisher.setBounds(209, 75, 95, 23);
		getContentPane().add(rdbtnPublisher);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (txtDocSearch.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please type card number");
					return;
				}
				
				ArrayList<ArrayList<Object>> result = m.execQuery("SELECT `id`, `title` FROM `DOCUMENT` WHERE `id` = " + txtDocSearch.getText() + ";");
				if (result == null || result.size() != 1) {
					JOptionPane.showMessageDialog(null, "Invalid card number");
				} else {
					System.out.println("valid");
				}
				
			}
		}
	);
		btnSearch.setBounds(327, 47, 117, 29);
		getContentPane().add(btnSearch);
		
		JLabel lblNewLabel = new JLabel("City Library");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(180, 19, 81, 26);
		getContentPane().add(lblNewLabel);
		
	}
}
