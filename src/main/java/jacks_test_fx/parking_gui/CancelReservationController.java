package jacks_test_fx.parking_gui;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CancelReservationController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	@FXML
	private Button cancelSpotBtn;
	@FXML
	private Button closeBtn;
	
	private Stage stage;
	private ParkingSpot currentSpot;
	
	@FXML
	/**
	 * Takes the data from the form and submits it to the database
	 * Checks to make sure that all forms are filled and have no warnings
	 * @author Spencer J Peck
	 */
	private void cancelReservation() {
		//Update ParkingSpot
		String sql = "UPDATE ParkingSpots SET isTaken = ?,userID = ? WHERE spotID = ?;";
		try(Connection conn = DBConnection.getConnection(); //Establish connection to Database
				PreparedStatement statement = conn.prepareStatement(sql)){

			//Prepare SQL Statement
			conn.setAutoCommit(false);
			statement.setInt(1, 0);
			statement.setNull(2, java.sql.Types.NULL);
			statement.setInt(3, currentSpot.getParkingID());
			statement.addBatch();

			statement.executeBatch();
			conn.commit();
			currentSpot.setTaken(false);
			showSuccessDialog();

		} catch(SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}
		close();

	}
	@FXML
	private void close() {
		stage.close();
	}

	/**
	 * Provides user feedback on a successful execution
	 * Creates a small popup window that states the positive outcome of the method.
	 *@author Spencer J Peck
	 */
	private void showSuccessDialog() {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Success");
	    alert.setHeaderText(null);
	    alert.setContentText("Reservation Canceled!");
	    alert.showAndWait();
	}
	/**
	 * Used to provide a closing method once this window has completed its role
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public void setCurrentSpot(ParkingSpot currentSpot) {
		this.currentSpot = currentSpot;
	}
	
}
