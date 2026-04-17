package jacks_test_fx.parking_gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminCarRegistrationController implements Initializable{

    @FXML
    private ComboBox<User> userComboBox;
    
    @FXML
    public void setSelection() {
    	ObservableList<User> selections = FXCollections.observableArrayList(fetchUsers());
    	userComboBox.setItems(selections);
    }
    
    public void initialize(URL location, ResourceBundle resources) {
		//Get DB cars change dtype to User rather than String
//		userComboBox.getItems().addAll("Apple", "Banana", "Cherry");
    	setSelection();
		
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

	@FXML
	private Button submit, newUserButton;
	@FXML
	private TextField model, make, year, plate;
	@FXML
    private Label makeWarn, modelWarn, yearWarn, plateWarn, submitWarn;

    @FXML
    void addNewUser(ActionEvent event) {
    	try {
			MainApp.setRoot("registerUserWindow","Parking Lot Managment System");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void retrieveInfo(ActionEvent event) {
    	submitWarn.setText("");
    	if(validFields()) {
			updateCarTable();
//			Car newCar = new Car(make.getText(), model.getText(), year.getText(), plate.getText(), userComboBox.getValue());
//			cars.add(newCar);
//			Stage stage = (Stage) submit.getScene().getWindow();
//			stage.close();
			try {
				MainApp.switchRoot("citation");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	private void updateCarTable(){
		
		User selectedUser = userComboBox.getValue();
		
		if (selectedUser == null) {
	        submitWarn.setText("* Please select a user");
	        return;
	    }
		
		String sql = "INSERT INTO Cars (make, model, year, plate, userID) VALUES (?, ?, ?, ?, ?);";
		try(Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql)){
				
				conn.setAutoCommit(false);
				
				statement.setString(1, make.getText());
				statement.setString(2, model.getText());
				statement.setString(3, year.getText());
				statement.setString(4, plate.getText());
				statement.setInt(5, selectedUser.getUserID());
				statement.executeUpdate();
				
				conn.commit();
			} catch(SQLException e) {
				System.out.println("DB Error: " + e.getMessage());
			}
	}
	
	private List<User> fetchUsers() {
    	List<User> usersList = new ArrayList<>();
    	String sql = "SELECT userID, firstName, lastName, address, phoneNumber, accessLevel FROM Users";
    	try(Connection conn = DBConnection.getConnection();
    			PreparedStatement statement = conn.prepareStatement(sql)){	
    			conn.setAutoCommit(true);
    			
    			ResultSet results = statement.executeQuery();
    			while(results.next()) {

    	            // User fields
    	            int userID = results.getInt("userID");
    	            String fname = results.getString("firstName");
    	            String lname = results.getString("lastName");
    	            String address = results.getString("address");
    	            String phone = results.getString("phoneNumber");
    	            int accessLevel = results.getInt("accessLevel");
    	            
    	            User user = new User(userID, fname, lname, address, phone, accessLevel);
    				usersList.add(user);
    			}
    		} catch(SQLException e) {
    			System.out.println("DB Error: " + e.getMessage());
    		}
    	return usersList;
    }

}
