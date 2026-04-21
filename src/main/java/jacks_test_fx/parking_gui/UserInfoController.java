package jacks_test_fx.parking_gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class UserInfoController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.print("Init");
		makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
		modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
		plateCol.setCellValueFactory(new PropertyValueFactory<>("plate"));
	};
	public void setupData(int userID, Stage stage) {
		setStage(stage);
		setUser(DBConnection.getUserFromDatabase(userID));
		populateRegisteredCars(getRegisteredCars());
		populateUserInfo();
	}
	@FXML
	private Label userIDOutputLbl;
	@FXML
	private Label firstNameOutputLbl;
	@FXML
	private Label lastNameOutputLbl;
	@FXML
	private Label addressOutputLbl;
	@FXML
	private Label phoneNumberOutputLbl;
	@FXML
	private TableView<Car> carTableView;
	@FXML 
	private TableColumn<Car, String> makeCol;
	@FXML 
	private TableColumn<Car, String> yearCol;
	@FXML 
	private TableColumn<Car, String> modelCol;
	@FXML 
	private TableColumn<Car, String> plateCol;
	@FXML
    private Button createCitationButton;
	
	private Stage stage;
	private User user;
	
	@FXML
    void createNewCitation(ActionEvent event) throws IOException{
		System.out.println("Create Citation Button Pressed!");
		SceneUtility.switchScene(event, "citation", "Citation");
		
    }
	
	private void populateRegisteredCars(ArrayList<Car> cars) {
		for(Car car : cars) {
			carTableView.getItems().add(car);
		}
	}
	
	@FXML
	/**
	 * Creates a collection of Car objects that are registered to the current UserID
	 * @return ArrayList of all Registered Cars for the current User
	 */
	private ArrayList<Car> getRegisteredCars(){
		String sql = "SELECT * FROM Cars where userID = ?";
		try(Connection conn = DBConnection.getConnection(); //Establish connection to Database
				PreparedStatement statement = conn.prepareStatement(sql)){

			//Prepare SQL Statement
			conn.setAutoCommit(true);
			statement.setInt(1, user.getUserID());

			//Get results of sql statement
			ResultSet sqlRows = statement.executeQuery();
			ArrayList<Car> cars = new ArrayList<Car>();
			
			//Take data from sql statement and create a collection of cars
		    while(sqlRows.next()) {
		    	cars.add(new Car(sqlRows.getString("make"),sqlRows.getString("model"),sqlRows.getString("year"),sqlRows.getString("plate"),user));
		    }
		    //Return collection of cars
		    return cars;

		} catch(SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		    return new ArrayList<Car>();
		}
		
	}
	private void populateUserInfo() {
		try {
			userIDOutputLbl.setText(Session.currentUser.getUserID()+ "");
			firstNameOutputLbl.setText(Session.currentUser.getFname());
			lastNameOutputLbl.setText(Session.currentUser.getLname());
			addressOutputLbl.setText(Session.currentUser.getAddress());
			phoneNumberOutputLbl.setText(Session.currentUser.getPhoneNum());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Used to provide a closing method once this window has completed its role
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
