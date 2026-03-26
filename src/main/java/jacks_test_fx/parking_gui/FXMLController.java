package jacks_test_fx.parking_gui;
/*
Put header here


 */

import java.io.IOException;
import java.net.URL;

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

public class FXMLController implements Initializable {
	
	@FXML private Button spot1;
	@FXML private Button spot2;
    
    @FXML
    private void openRegistration(ActionEvent event) throws IOException {
    	Button currentSpot = (Button) event.getSource();
    	ParkingSpot currentSpace = (ParkingSpot) currentSpot.getUserData();
    	if(!currentSpace.isTaken()) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
            Parent parent = loader.load();
            RegistrationController rControl = loader.getController();
            rControl.readParkingSpot(currentSpace);
            
            Stage stage = new Stage();
            stage.setTitle("Register your Car");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            
            Window owner = ((Button) event.getSource()).getScene().getWindow();
            stage.initOwner(owner);
            stage.showAndWait();
            
            String enteredModel = rControl.getModel();
            System.out.println(enteredModel);
    	} 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	ParkingSpot parkingSpace1 = new ParkingSpot(ParkingType.NORMAL, ParkingValues.STANDARD);
    	ParkingSpot parkingSpace2 = new ParkingSpot(ParkingType.HANDICAP, ParkingValues.CHEAP);
    	attachButtonListener(spot1, parkingSpace1);
    	attachButtonListener(spot2, parkingSpace2);
    }

	/**
	 * Attaches a listener to the isTaken field in parking spot object of each button
	 */
	private void attachButtonListener(Button spot, ParkingSpot parkingSpace) {
		spot.setUserData(parkingSpace);
		parkingSpace.takenProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					spot.setStyle("-fx-background-color: #ff0000");
				} else {
					spot.setStyle("-fx-background-color: #00ff00");
				}
			}
		});
		
		if(parkingSpace.isTaken()) {
			spot.setStyle("-fx-background-color: #ff0000");
		} else {
			spot.setStyle("-fx-background-color: #00ff00");
		}
	}    
}
