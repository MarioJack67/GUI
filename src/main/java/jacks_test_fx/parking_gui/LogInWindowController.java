package jacks_test_fx.parking_gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LogInWindowController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addTxtFldListener(fNameTxtFld, fNameWarnLbl);
		addTxtFldListener(lNameTxtFld, lNameWarnLbl);
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
	private TextField phoneNumTxtFld;
	@FXML
	private Label phoneNumWarnLbl;
	
	private User loggedUser;
	
	/**
	 * Taking data from the form this method checks for a user in the related database.
	 * If a related User exists, it sets loggedUser to the related information.
	 * @author Spencer J Peck
	 */
	private void getUserFromDatabase() {
		if(metRequirements()) {
			//Insert the User into SQL Statement
			String sql = "SELECT * FROM Users WHERE firstName = ? AND lastName = ? AND phoneNumber = ?";
			try(Connection conn = DriverManager.getConnection(FXMLController.databaseURL); //Establish connection to Database
					
					PreparedStatement statement = conn.prepareStatement(sql)){

				//Prepare SQL Statement
				conn.setAutoCommit(true);
				statement.setString(1, fNameTxtFld.getText());
				statement.setString(2, lNameTxtFld.getText());
				statement.setString(3, phoneNumTxtFld.getText());


				ResultSet returnedUser =statement.executeQuery();
				if(!returnedUser.next()) {showFailDialog("No associated User found");}
				setLoggedUser(returnedUser);

			} catch(SQLException e) {
				System.out.println("DB Error: " + e.getMessage());
			}
		}
		else {showFailDialog("Please enter in data");}

	}
	/**
	 * Given a row from the Users table this method assigns the data to the loggedUser field.
	 * @param returnedUser
	 * @throws SQLException
	 * @author Spencer J Peck
	 */
	private void setLoggedUser(ResultSet returnedUser) throws SQLException {
		while(returnedUser.next()) {
			loggedUser = new User(returnedUser.getInt("userID"),
					returnedUser.getString("firstName"),
					returnedUser.getString("lastName"),
					returnedUser.getString("address"),
					returnedUser.getString("phoneNumber"),
					returnedUser.getInt("accessLevel"));
		}
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
	private void showFailDialog(String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Error");
	    alert.setHeaderText(null); // optional — removes the header
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	public User getLoggedUser() {
		return loggedUser;
	}
	/*
	////////////////////
	///Register User///
	//////////////////
    @FXML
    private void openUserRegistration(ActionEvent event) throws IOException {
    		//Load registration GUI and its associated controller
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registerUserWindow.fxml"));
            Parent parent = loader.load();
            RegisterUserController rControl = loader.getController();
            
            Stage stage = new Stage();
            stage.setTitle("Register your information");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            rControl.setStage(stage);
            
            Window owner = ((Button) event.getSource()).getScene().getWindow();
            stage.initOwner(owner);
            stage.showAndWait();
    	
    }*/
	
	
}
