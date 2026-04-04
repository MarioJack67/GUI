package jacks_test_fx.parking_gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * This class serves as the primary controller for the Parking Lot GUI, including buttons and DB
 * The buttons are attached to a ParkingSpot object that has a boolean listener to update the GUI
 * There are also several functions to retrieve and update data in the SQLite DB
 * 
 * @author Jack B
 */
public class FXMLController implements Initializable {
	
	//Buttons and lists from the GUI itself
	@FXML private Button spot1, spot2, spot3, spot4, spot5, spot6, spot7, spot8, spot9, spot10, spot11, spot12;
	List<Button> parkingButtons = new ArrayList<>();
	List<ParkingSpot> parkingSpaces = new ArrayList<>();
	
    @FXML
    private void openRegistration(ActionEvent event) throws IOException {
    	Button currentSpot = (Button) event.getSource(); //find button that was clicked
    	//get ParkingSpot object attached to the clicked button
    	ParkingSpot currentSpace = (ParkingSpot) currentSpot.getUserData();
    	if(!currentSpace.isTaken()) {
    		//Load registration GUI and its associated controller
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
            Parent parent = loader.load();
            RegistrationController rControl = loader.getController();
            rControl.readParkingSpot(currentSpace); //pass ParkingSpot object to rControl
            
            Stage stage = new Stage();
            stage.setTitle("Register your Car");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            
            Window owner = ((Button) event.getSource()).getScene().getWindow();
            stage.initOwner(owner);
            stage.showAndWait();
            
            //As a small tests, fetches and prints car model from the registration screen
            String enteredModel = rControl.getModel();
            System.out.println(enteredModel);
            updateParkingTable();
    	} 
    }
    
    @Override
    /**
     * Each time the GUI loads, this method will run, overrided from Initializable interface
     * It fetches each ParkingSpot from the DB and attaches the buttons to those objects
     * while still remembering which spots were already reserved.
     * @param url - The URL to load up
     * @param rb - Any resource bundles that need to be included.
     */
    public void initialize(URL url, ResourceBundle rb) {
    	parkingButtons.addAll(Arrays.asList(spot1, spot2, spot3, spot4, spot5, spot6, spot7, spot8, spot9, spot10, spot11, spot12));
    	for(int i = 0; i < parkingButtons.size(); i++) {
    		parkingSpaces.add(fetchParkingTable(i + 1));
    		attachButtonListener(parkingButtons.get(i), parkingSpaces.get(i));
    	}
    }

	/**
	 * Attaches a listener to the isTaken field in parking spot object of each button (Refactored)
	 * @param spot - A JavaFX button that will be attached to a ParkingSpot object
	 * @param parkingSpace - The object that stores specific data on a particular parking space button
	 */
	private void attachButtonListener(Button spot, ParkingSpot parkingSpace) {
		spot.setUserData(parkingSpace); //bind button and ParkingSpot
		parkingSpace.takenProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					spot.setStyle("-fx-background-color: #8B0000");
				} else {
					spot.setStyle("-fx-background-color: #8B0000");
				}
			}
		});
		
		if(parkingSpace.isTaken()) {
			spot.setStyle("-fx-background-color: #8B0000"); //set to dark red
		} else {
			spot.setStyle("-fx-background-color: #2C4C3B"); //set to dark green
		}
	} 
	
	@FXML
	/**
	 * This functions specifically updates the ParkingSpots table in the DB when called
	 * It updates all values in the table for a single record and updates an entire batch
	 * so that it doesn't have to be called several times for multiple objects
	 */
	private void updateParkingTable() {
		String url = "jdbc:sqlite:C:/Users/dange/git/GUI/src/main/resources/parkingDB.sqlite";
		String sql = "INSERT INTO ParkingSpots (spotID, type, value, isTaken) VALUES (?, ?, ?, ?) ON CONFLICT(spotID) DO UPDATE SET isTaken=excluded.isTaken;";
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement statement = conn.prepareStatement(sql)){
			
			conn.setAutoCommit(true);
			for(ParkingSpot el : parkingSpaces) {
				statement.setInt(1, el.getParkingID());
				statement.setString(2, el.getType().name());
				statement.setString(3, el.getValue().name());
				statement.setBoolean(4, el.isTaken());
				statement.addBatch();
				
			}
			statement.executeBatch();
		} catch(SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}
		
	}
	
	/**
	 * Returns an single, individual ParkingSpot from the DB based on the associated id
	 * @param id - The unique key to find the specific ParkingSpot object in DB
	 * @return A new ParkingSpot object populated with it's same data from the DB
	 */
	private ParkingSpot fetchParkingTable(int id) {
		String url = "jdbc:sqlite:C:/Users/dange/git/GUI/src/main/resources/parkingDB.sqlite";
		String sql = "SELECT * FROM ParkingSpots WHERE spotID = ?;";
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement statement = conn.prepareStatement(sql)){
			
			statement.setInt(1, id);
			try(ResultSet results = statement.executeQuery()){
				if(results.next()) {
					return new ParkingSpot(results.getInt("spotID"), ParkingType.valueOf(results.getString("type").toUpperCase()), ParkingValues.valueOf(results.getString("value").toUpperCase()), results.getBoolean("isTaken"));
				}
			}
		} catch(SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}
		return null;
	}
}
