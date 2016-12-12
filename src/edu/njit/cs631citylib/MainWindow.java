package edu.njit.cs631citylib;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import java.awt.Color;

public class MainWindow {

	private JFrame frmCityLibrary;
	private JTextField txtCardNumber;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmCityLibrary.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Initialize MySQL connection
		DBManager m = DBManager.getInstance();
		if (!m.connect()) {
			JOptionPane.showMessageDialog(null, "Could not connect to database");
			System.exit(1);
		}

		frmCityLibrary = new JFrame();
		frmCityLibrary.setTitle("CITY LIBRARY");
		frmCityLibrary.getContentPane().setBackground(Color.WHITE);
		frmCityLibrary.getContentPane().setForeground(Color.WHITE);
		frmCityLibrary.setBounds(100, 100, 450, 300);
		frmCityLibrary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCityLibrary.getContentPane().setLayout(null);
		
		JLabel lblCardNumber = new JLabel("Card Number");
		lblCardNumber.setBounds(38, 69, 90, 16);
		frmCityLibrary.getContentPane().add(lblCardNumber);
		
		txtCardNumber = new JTextField();
		txtCardNumber.setBounds(125, 64, 130, 26);
		frmCityLibrary.getContentPane().add(txtCardNumber);
		txtCardNumber.setColumns(10);
		
		JButton btnReaderLogin = new JButton("Login");
		btnReaderLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtCardNumber.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please type card number");
					return;
				}

				ArrayList<ArrayList<Object>> result = m.execQuery("SELECT `READERID`, `RTYPE` FROM `READER` WHERE `READERID` = " + txtCardNumber.getText() + ";");
				if (result == null || result.size() != 1) {
					JOptionPane.showMessageDialog(null, "Invalid card number");
				} else {
					Reader dialog = new Reader(txtCardNumber.getText());
					dialog.setModal(true);
					dialog.setVisible(true);
				}
			}
		});
		btnReaderLogin.setBounds(280, 64, 79, 29);
		frmCityLibrary.getContentPane().add(btnReaderLogin);
		
		JLabel lblCityLibrary = new JLabel("READER");
		lblCityLibrary.setForeground(Color.BLACK);
		lblCityLibrary.setBackground(Color.GRAY);
		lblCityLibrary.setBounds(38, 36, 71, 16);
		frmCityLibrary.getContentPane().add(lblCityLibrary);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 119, 438, 16);
		frmCityLibrary.getContentPane().add(separator);
	}

}
