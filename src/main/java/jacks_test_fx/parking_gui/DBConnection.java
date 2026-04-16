package jacks_test_fx.parking_gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	/**
	 * Given a userID this method checks for a user in the related database.
	 * If a related User exists, it returns a User Object that reflects that info.
	 * @author Spencer J Peck
	 */
	public static User getUserFromDatabase(int userId) {
			//Select the User into SQL Statement
			String sql = "SELECT * FROM Users WHERE userId = ?";
			try(Connection conn = DBConnection.getConnection(); //Establish connection to Database
					
					PreparedStatement statement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){

				//Prepare SQL Statement
				conn.setAutoCommit(true);
				statement.setInt(1, userId);


				return createUser(statement.executeQuery());
				
			} catch(SQLException e) {
				System.out.println("DB Error: " + e.getMessage());
				return new User(-1,"Invalid","Invalid","Invalid","Invalid", 0);
			}

	}
	/**
	 * Given a row from the Users table this method assigns the data to a new User Object.
	 * @param returnedUser
	 * @throws SQLException
	 * @author Spencer J Peck
	 */
	private static User createUser(ResultSet returnedUser) throws SQLException {
		while(returnedUser.next()) {
			return new User(returnedUser.getInt("userID"),
					returnedUser.getString("firstName"),
					returnedUser.getString("lastName"),
					returnedUser.getString("address"),
					returnedUser.getString("phoneNumber"),
					returnedUser.getInt("accessLevel"));
		}
		return new User(-1,"Invalid","Invalid","Invalid","Invalid", 0);
	}

}