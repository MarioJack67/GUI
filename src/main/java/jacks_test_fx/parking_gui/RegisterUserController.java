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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterUserController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addTxtFldListener(fNameTxtFld, fNameWarnLbl);
		addTxtFldListener(lNameTxtFld, lNameWarnLbl);
		addTxtFldListener(addressTxtFld, addressWarnLbl);
		addPhoneNumberTextFieldListener(phoneNumTxtFld, phoneNumWarnLbl);
		
	}
	@FXML 
	private TextField fNameTxtFld;
	@FXML
	private Label fNameWarnLbl;
	@FXML
	private TextField lNameTxtFld;
	@FXML
	private Label lNameWarnLbl;
	@FXML
	private TextField addressTxtFld;
	@FXML
	private Label addressWarnLbl;
	@FXML
	private TextField phoneNumTxtFld;
	@FXML
	private Label phoneNumWarnLbl;
	
	private Stage stage;
	
	@FXML
	/**
	 * Takes the data from the form and submits it to the database
	 * Checks to make sure that all forms are filled and have no warnings
	 * @author Spencer J Peck
	 */
	private void submitUser() {
		if(metRequirements()) {
			//Insert the User into SQL Statement
			String sql = "SELECT userID FROM Users (firstName, LastName, phoneNumber) VALUES (?, ?, ?, ?) ON CONFLICT(userID) DO UPDATE SET isTaken=excluded.isTaken;";
			try(Connection conn = DriverManager.getConnection(FXMLController.databaseURL); //Establish connection to Database
					PreparedStatement statement = conn.prepareStatement(sql)){

				//Prepare SQL Statement
				conn.setAutoCommit(true);
				statement.setString(1, fNameTxtFld.getText());
				statement.setString(2, lNameTxtFld.getText());
				statement.setString(4, phoneNumTxtFld.getText());
				statement.addBatch();


				statement.executeBatch();
				showSuccessDialog();
			    stage.close();

			} catch(SQLException e) {
				System.out.println("DB Error: " + e.getMessage());
			}
		}
		else {showFailDialog();}

	}

	/**
	 * Adds a listener to a TextField to provide real-time data validation
	 * @param txtfld The TextField to be monitored
	 * @param warningLabel The Label to be updated when TexField is invalid
	 * @author Spencer J Peck
	 */
	private void addTxtFldListener(TextField txtfld, Label warningLabel) {
		txtfld.textProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue == null || newValue.trim().isEmpty()) {
	        	warningLabel.setText("This is required.");
	        } else {
	        	warningLabel.setText(""); // Clear warning
	        }
	    });
	}
	/**
	 * Adds a listener to a TextField to provide real-time data validation
	 * Provides data validation for phone numbers
	 * @param txtfld The TextField to be monitored
	 * @param warningLabel The Label to be updated when TexField is invalid
	 * @author Spencer J Peck
	 */
	private void addPhoneNumberTextFieldListener(TextField txtfld, Label warningLabel) {
		txtfld.textProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue == null || newValue.trim().isEmpty()) {
	        	warningLabel.setText("This is required.");
	        }
	        else if (!(User.validatePhoneNum(newValue))) {
	        	warningLabel.setText("Not a Valid Phone Number");
	        }
	        else {
	        	warningLabel.setText(""); // Clear warning
	        }
	    });
	}
	/**
	 * Checks to see if all of the required fields are filled out.
	 * Utilizes the textfield listeners to check the output of the warnings for each Text Field. If anything is present in the warnings, this method returns false
	 * Some research into a better way may be needed.
	 * @return True if no errors are present, False if something is wrong.
	 * 
	 * @author Spencer J Peck
	 */
	private boolean metRequirements() {
		if(!(fNameWarnLbl.getText() == "")) {
			return false;
		}
		if(!(lNameWarnLbl.getText() == "")) {
			return false;
		}
		if(!(addressWarnLbl.getText() == "")) {
			return false;
		}
		if(!(phoneNumWarnLbl.getText() == "")) {
			return false;
		}
		return true;		
	}
	/**
	 * Provides user feedback on a successful execution
	 * Creates a small popup window that states the positive outcome of the method.
	 *@author Spencer J Peck
	 */
	private void showSuccessDialog() {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Success");
	    alert.setHeaderText(null); // optional — removes the header
	    alert.setContentText("User registered successfully!");
	    alert.showAndWait();
	}
	/**
	 * Provides user feedback on a failed execution
	 * Creates a small popup window that states that something went wrong.
	 *@author Spencer J Peck
	 */
	private void showFailDialog() {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Error");
	    alert.setHeaderText(null); // optional — removes the header
	    alert.setContentText("Invalid Data");
	    alert.showAndWait();
	}
	
	/**
	 * Used to provide a closing method once this window has completed its role
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
