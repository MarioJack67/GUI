package jacks_test_fx.parking_gui;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CarRegistrationController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addTxtFldListener(model, modelWarn);
		addTxtFldListener(make, makeWarn);
		
		//Year Listener
		year.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null || newValue.trim().isEmpty()){
				yearWarn.setText("* Required Field");
			}
			else if(!(newValue.matches("\\d+"))) {
	        	yearWarn.setText("* Invalid Year");
	        } else {
	        	yearWarn.setText("*"); // Clear warning
	        }
	    });
		
		//Plate Listener
		plate.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null || newValue.trim().isEmpty()){
				plateWarn.setText("* Required Field");
			}
			else if (newValue.length() > 7) {
	        	plateWarn.setText("* Invalid Plate");
	        } else {
	        	plateWarn.setText("*"); // Clear warning
	        }
	    });
	}
	
	private User currentUser;
	private ObservableList<Car> cars;
	
	@FXML
	private TextField model;
	@FXML
	private Button submit;
	@FXML
	private TextField make;
	@FXML
	private TextField year;
	@FXML
	private TextField plate;
	
	@FXML
    private Label makeWarn;
	@FXML
    private Label modelWarn;
	@FXML
    private Label yearWarn;
	@FXML
    private Label plateWarn;
	@FXML
    private Label submitWarn;
	
	@FXML
	private void retrieveInfo(ActionEvent event) {
		if(validFields()) {
			updateCarTable();
			Car newCar = new Car(make.getText(), model.getText(), year.getText(), plate.getText(), currentUser);
			cars.add(newCar);
			Stage stage = (Stage) submit.getScene().getWindow();
			stage.close();
		}
	}
	
	
	/**
	 * Checks if all text entry fields contain valid user input and returns true if so.
	 * @author John Gabriel Allen
	 * @return
	 */
	private boolean validFields() {
		int check = 0;
		
		if(!(model.getText().equals(""))) { check++; } else { modelWarn.setText("* Required Field"); }
		if(!(make.getText().equals(""))) { check++; } else { makeWarn.setText("* Required Field"); }
		if(!(year.getText().equals(""))) { 
			if(year.getText().matches("\\d+")) { check++; }
			else { yearWarn.setText("* Must be numeric"); }
		} else { yearWarn.setText("* Required Field"); }
		if(!(plate.getText().equals(""))){ 
			if(plate.getText().length() < 8) { check++; }
			else { plateWarn.setText("* Cannot exceed 7 characters"); }
		}
		else { plateWarn.setText("* Required Field"); }
		
		if(check == 4) { return true; }
		submitWarn.setText("* One or more Invalid Fields!");
		return false;		
	}
	
	/**
	 * Adds a listener to a TextField to provide real-time data validation
	 * @param txtfld The TextField to be monitored
	 * @param warningLabel The Label to be updated when TexField is invalid
	 * @author Spencer J Peck, John Gabriel Allen
	 */
	private void addTxtFldListener(TextField txtfld, Label warningLabel) {
		txtfld.textProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue == null || newValue.trim().isEmpty()) {
	        	warningLabel.setText("* Required Field");
	        } else {
	        	warningLabel.setText("*"); // Clear warning
	        }
	    });
	}
	
	public void readUser(User user) {
    	this.currentUser = user;
    }
	
	public void readCars(ObservableList<Car> cars) {
		this.cars = cars;
	}
	
	private void updateCarTable(){
		String sql = "INSERT INTO Cars (make, model, year, plate, userID) VALUES (?, ?, ?, ?, ?);";
		try(Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql)){
				
				conn.setAutoCommit(false);
				
				statement.setString(1, make.getText());
				statement.setString(2, model.getText());
				statement.setString(3, year.getText());
				statement.setString(4, plate.getText());
				statement.setInt(5, currentUser.getUserID());
				statement.addBatch();
					
				statement.executeBatch();
				conn.commit();
			} catch(SQLException e) {
				System.out.println("DB Error: " + e.getMessage());
			}
	}

}
