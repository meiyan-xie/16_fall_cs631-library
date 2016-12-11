package edu.njit.cs631citylib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBManager {
	private static DBManager dbManager = null;
	private static Connection dbConnection = null;

	public static DBManager getInstance() {
		if (dbManager == null) dbManager = new DBManager();
		return dbManager;
	}

	public boolean connect() {
		// Check if already connected
		if (dbConnection != null) return true;

		// Define variables
		String jdbc_url = "jdbc:mysql://meiyan.blupig.net:3306/citylib?useSSL=false"; 
		String username = "root";
		String password = "meiyan";

		try {
			// Attempt to connect
			Connection connection = DriverManager.getConnection(jdbc_url, username, password);

			// Assign to static variable
		    dbConnection = connection;
		} catch (SQLException e) {
			// Failed to connect
			e.printStackTrace();
			return false;
		}
		// Success
		return true;
	}

	public boolean disConnect() {
		if (dbConnection == null) return true;
		try {
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public ArrayList<ArrayList<Object>> execQuery(String sql) {
		// Check connection
		if (dbConnection == null) return null;

		// Initialize result array
		ArrayList<ArrayList<Object>> arr = new ArrayList<ArrayList<Object>>();
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int columnCount = rs.getMetaData().getColumnCount();

			// Iterate through all rows
			while (rs.next()) {
				ArrayList<Object> row = new ArrayList<Object>();
				for (int c = 1; c <= columnCount; c++) {
					row.add(rs.getObject(c));
				}
				arr.add(row);
			}

			// Release resources
			rs.close();
			stmt.close();
			
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
