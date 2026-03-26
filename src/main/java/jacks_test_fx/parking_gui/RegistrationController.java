package jacks_test_fx.parking_gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	private ParkingSpot currentSpot;
	
	@FXML
	private TextField model;
	@FXML
	private Button submit;
	private String modelValue;
	
	@FXML
	private void retrieveInfo(ActionEvent event) {
		modelValue = model.getText();
		currentSpot.setTaken(true);
		Stage stage = (Stage) submit.getScene().getWindow();
		stage.close();
	}
	
	public String getModel() {
		return modelValue;
	}
	
	public void readParkingSpot(ParkingSpot currentSpot) {
		this.currentSpot = currentSpot;
	}

}
