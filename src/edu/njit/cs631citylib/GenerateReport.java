package edu.njit.cs631citylib;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;


public class GenerateReport extends JDialog{
	public static void main(String[] args) {
		try {
			GenerateReport dialog = new GenerateReport();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
public GenerateReport() {
		
		
        
		
        getContentPane().setBackground(Color.WHITE);
		//Connect to Database
		DBManager m = DBManager.getInstance();
		m.connect();
	
		setBounds(100, 100, 500, 550);
		getContentPane().setLayout(null);
		
		JLabel lblGenerateReport = new JLabel("Generate Reports");
		lblGenerateReport.setBounds(38, 14, 200, 31);
		getContentPane().add(lblGenerateReport);
		
		JButton btnBranchInfo = new JButton("Print Branch Info");
		btnBranchInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BranchInfo dialog = new BranchInfo();
				dialog.setModal(true);
				dialog.setVisible(true);
				}
		});
		btnBranchInfo.setBounds(38, 64, 200, 29);
		getContentPane().add(btnBranchInfo);
			
	JButton btnTopR = new JButton("Print top 10 Readers");
	btnTopR.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TopR dialog = new TopR();
					dialog.setModal(true);
					dialog.setVisible(true);
					}	
    });	
	btnTopR.setBounds(38, 114, 200, 29);
	getContentPane().add(btnTopR);
    
    JButton btnTopB = new JButton("Print top 10 borrowed books");
    btnTopB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TopB dialog = new TopB();
					dialog.setModal(true);
					dialog.setVisible(true);
					}	
    });	
    btnTopB.setBounds(38, 164, 200, 29);
	getContentPane().add(btnTopB);
	
	JButton btnTopP = new JButton("Print top 10 popular books");
	btnTopP.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TopP dialog = new TopP();
					dialog.setModal(true);
					dialog.setVisible(true);
					}	
    });	
	btnTopP.setBounds(38, 214, 200, 29);
	getContentPane().add(btnTopP);
	
	JButton btnAvFn = new JButton("Print Average Fine per user");
	btnAvFn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AvFn dialog = new AvFn();
					dialog.setModal(true);
					dialog.setVisible(true);
					}	
    });	
	btnAvFn.setBounds(38, 264, 200, 29);
	getContentPane().add(btnAvFn);
		
}

}
