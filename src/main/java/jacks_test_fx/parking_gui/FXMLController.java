package jacks_test_fx.parking_gui;

import java.io.IOException;

import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	public static String databaseURL = "srv526.hstgr.io";
	
	//Buttons and lists from the GUI itself
	@FXML private Button spot1, spot2, spot3, spot4, spot5, spot6, spot7, spot8, spot9, spot10, spot11, spot12, spot13, spot14, spot15, spot16, spot17, spot18, spot19, spot20, spot21, spot22, spot23, spot24;
	@FXML private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11, image12, image13, image14, image15, image16, image17, image18, image19, image20, image21, image22, image23, image24;
	List<Button> parkingButtons = new ArrayList<>();
	List<ParkingSpot> parkingSpaces = new ArrayList<>();
	List<ImageView> images = new ArrayList<>();
	Image carImage = new Image(getClass().getResourceAsStream("/car.png"));
	Stage stage;
	User currentUser;
	
    @FXML
    private void userUseParkingSpace(ActionEvent event) throws IOException {
    	Button currentSpot = (Button) event.getSource(); //find button that was clicked
    	//get ParkingSpot object attached to the clicked button
    	ParkingSpot currentSpace = (ParkingSpot) currentSpot.getUserData();
    	if(!currentSpace.isTaken()) {
            Session.parkingSpot = currentSpace;
            System.out.println("Session.parkingSpot Updated: " + Session.parkingSpot);
            SceneUtility.popoutScene(event, "carSelection", "Select Your Car");
            
            //As a small tests, fetches and prints car model from the registration screen
            updateParkingTable(currentSpace.getParkingID());
    	}else {
    		if(currentSpace.getUserID() == currentUser.getUserID()) {
                SceneUtility.popoutScene(event, "unregisterParkingSpot", "Cancel Your Reservation?");
        		openCancelRegistration(event, currentSpace);    			
    		}
    	}

    }

	private void openRegistration(ActionEvent event, ParkingSpot currentSpace) throws IOException {
		//Load registration GUI and its associated controller
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/carSelection.fxml"));
		Parent parent = loader.load();
		CarSelectionController sControl = loader.getController();
		sControl.readParkingSpot(currentSpace); //pass ParkingSpot object to sControl
		sControl.readUser(currentUser);
		sControl.setSelection();
		
		Stage stage = new Stage();
		stage.setTitle("Select your Car");
		stage.setScene(new Scene(parent));
		stage.initModality(Modality.WINDOW_MODAL);
		
		Window owner = ((Button) event.getSource()).getScene().getWindow();
		stage.initOwner(owner);
		stage.showAndWait();
		
		//As a small tests, fetches and prints car model from the registration screen
		updateParkingTable(currentSpace.getParkingID());
	}
	
	private void openCancelRegistration(ActionEvent event, ParkingSpot currentSpace) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/unregisterParkingSpot.fxml"));
        Parent parent = loader.load();
        CancelReservationController control = loader.getController();
        
        Stage stage = new Stage();
        stage.setTitle("Cancel Your Reservation?");
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.WINDOW_MODAL);
        control.setStage(stage);
        control.setCurrentSpot(currentSpace);
        
        Window owner = ((Button) event.getSource()).getScene().getWindow();
        stage.initOwner(owner);
        stage.showAndWait();

	}
    
    @FXML
    private void adminUseParkingSpace(ActionEvent event) throws IOException {
    	Button currentSpot = (Button) event.getSource(); //find button that was clicked
    	//get ParkingSpot object attached to the clicked button
    	ParkingSpot currentSpace = (ParkingSpot) currentSpot.getUserData();
    	if(currentSpace.isTaken()) {
    		//update user space info
    		Session.viewedUser =  DBConnection.getUserFromDatabase(currentSpace.getUserID()); //currentSpace.getUserID();
    		System.out.println("Current Space User Updated: " + Session.viewedUser);
    		SceneUtility.popoutScene(event, "userInfoWindow", "User Info");
    	} 
    }

	private void showUserInfo(ActionEvent event, ParkingSpot currentSpace) throws IOException {
		//Load registration GUI and its associated controller
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userInfoWindow.fxml"));
		Parent parent = loader.load();
		UserInfoController userInfoController = loader.getController();
		userInfoController.setupData(currentSpace.getUserID(), stage);
		
		Stage stage = new Stage();
		stage.setScene(new Scene(parent));
		stage.initModality(Modality.WINDOW_MODAL);
		
		Window owner = ((Button) event.getSource()).getScene().getWindow();
		stage.initOwner(owner);
		stage.showAndWait();
	}
    @FXML
    private void useParkingSpace(ActionEvent event) throws IOException {
    	switch (Session.currentUser.getAccessLevel()){
    	case 1:
    		adminUseParkingSpace(event);
    		break;
    	default :
    		userUseParkingSpace(event);
    		
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
    	parkingButtons.addAll(Arrays.asList(spot1, spot2, spot3, spot4, spot5, spot6, spot7, spot8, spot9, spot10, spot11, spot12, spot13, spot14, spot15, spot16, spot17, spot18, spot19, spot20, spot21, spot22, spot23, spot24));
    	images.addAll(Arrays.asList(image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11, image12, image13, image14, image15, image16, image17, image18, image19, image20, image21, image22, image23, image24));
    	try(Connection conn = DBConnection.getConnection()){
    		for(int i = 0; i < parkingButtons.size(); i++) {
        		parkingSpaces.add(fetchParkingTable(i + 1, conn));
        		attachButtonListener(parkingButtons.get(i), parkingSpaces.get(i), images.get(i));
        	}
    	} catch(SQLException e) {
    		System.out.println("DB Error: " + e.getMessage());
    	}

    }

	/**
	 * Attaches a listener to the isTaken field in parking spot object of each button (Refactored)
	 * @param spot - A JavaFX button that will be attached to a ParkingSpot object
	 * @param parkingSpace - The object that stores specific data on a particular parking space button
	 */
	private void attachButtonListener(Button spot, ParkingSpot parkingSpace, ImageView image) {
		spot.setUserData(parkingSpace); //bind button and ParkingSpot
		spot.setGraphic(image); //bind button and image
		parkingSpace.takenProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					spot.setStyle("-fx-background-color: #8B0000");
					image.setImage(carImage);
				} else {
					spot.setStyle("-fx-background-color: #2C4C3B");
					image.setImage(null);
				}
			}
		});
		
		if(parkingSpace.isTaken()) {
			spot.setStyle("-fx-background-color: #8B0000"); //set to dark red
			image.setImage(carImage);
		} else {
			spot.setStyle("-fx-background-color: #2C4C3B"); //set to dark green
			image.setImage(null);
		}
	} 
	
	/**
	 * This functions specifically updates the ParkingSpots table in the DB when called
	 * It updates all values in the table for a single record and updates an entire batch
	 * so that it doesn't have to be called several times for multiple objects
	 */
	private void updateParkingTable(int id) {
		String sql = "INSERT INTO ParkingSpots (spotID, type, value, isTaken, userID) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE isTaken=VALUES(isTaken), userID=VALUES(userID);";
		try(Connection conn = DBConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)){
			
			conn.setAutoCommit(false);
			ParkingSpot chosenSpace = parkingSpaces.get(id - 1);
			
			statement.setInt(1, chosenSpace.getParkingID());
			statement.setString(2, chosenSpace.getType().name());
			statement.setString(3, chosenSpace.getValue().name());
			statement.setBoolean(4, chosenSpace.isTaken());
			statement.setInt(5, Session.currentUser.getUserID());
			statement.addBatch();
				
			statement.executeBatch();
			conn.commit();
			chosenSpace.setUserID(currentUser.getUserID());
		} catch(SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}	
	}
	
	/**
	 * Returns an single, individual ParkingSpot from the DB based on the associated id
	 * @param id - The unique key to find the specific ParkingSpot object in DB
	 * @return A new ParkingSpot object populated with it's same data from the DB
	 */
	private ParkingSpot fetchParkingTable(int id, Connection conn) {
		String sql = "SELECT * FROM ParkingSpots WHERE spotID = ?;";
		try(PreparedStatement statement = conn.prepareStatement(sql)){
			statement.setInt(1, id);
			try(ResultSet results = statement.executeQuery()){
				if(results.next()) {
					return new ParkingSpot(results.getInt("spotID"), ParkingType.valueOf(results.getString("type").toUpperCase()), ParkingValues.valueOf(results.getString("value").toUpperCase()), results.getBoolean("isTaken"),results.getInt("userID"));
				}
			}
		} catch(SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Used to provide a closing method once this window has completed its role
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setUser(User user) {
		currentUser = user;
	}
}
