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

public class MainWindow {

	private JFrame frame;
	private JTextField txtCardNumber;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
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

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblCardNumber = new JLabel("Card Number");
		lblCardNumber.setBounds(39, 51, 90, 16);
		frame.getContentPane().add(lblCardNumber);
		
		txtCardNumber = new JTextField();
		txtCardNumber.setBounds(125, 46, 130, 26);
		frame.getContentPane().add(txtCardNumber);
		txtCardNumber.setColumns(10);
		
		JButton btnReaderLogin = new JButton("Login");
		btnReaderLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtCardNumber.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please type card number");
					return;
				}

				ArrayList<ArrayList<Object>> result = m.execQuery("SELECT `id`, `type` FROM `READER` WHERE `id` = " + txtCardNumber.getText() + ";");
				if (result == null || result.size() != 1) {
					JOptionPane.showMessageDialog(null, "Invalid card number");
				} else {
					System.out.println("valid");
				}
			}
		});
		btnReaderLogin.setBounds(279, 46, 79, 29);
		frame.getContentPane().add(btnReaderLogin);
	}

}
