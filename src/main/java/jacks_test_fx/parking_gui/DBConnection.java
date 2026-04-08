package jacks_test_fx.parking_gui;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	private static final String URL = "jdbc:mysql://srv526.hstgr.io:3306/u425333665_parking_DB";
	private static final String USER = "u425333665_parkingMaster";
	private static final String PASSWORD = "Spring2450";

	public static Connection getConnection() {
		Connection con = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connected to database!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

}