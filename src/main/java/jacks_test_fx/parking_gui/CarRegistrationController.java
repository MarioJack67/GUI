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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CarRegistrationController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
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
	private void retrieveInfo(ActionEvent event) {
		updateCarTable();
		Car newCar = new Car(make.getText(), model.getText(), year.getText(), plate.getText(), currentUser);
		cars.add(newCar);
		Stage stage = (Stage) submit.getScene().getWindow();
		stage.close();
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
