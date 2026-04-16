package jacks_test_fx.parking_gui;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserInfoController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.print("Init");
	};
	public void setupData(User user, Stage stage) {
		setStage(stage);
		setUser(user);
		populateRegisteredCars(getRegisteredCars());
		getUserFromDatabase();
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
	
	private Stage stage;
	private User user;
	
	private void populateRegisteredCars(ArrayList<Car> cars) {
		for(Car car : cars) {
			carTableView.getItems().add(car);
		}
	}
	
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
	/**
	 * Taking data from the form this method checks for a user in the related database.
	 * If a related User exists, it sets loggedUser to the related information.
	 * @author Spencer J Peck
	 */
	private void getUserFromDatabase() {
			//Select the User into SQL Statement
			String sql = "SELECT * FROM Users WHERE userId = ?";
			try(Connection conn = DBConnection.getConnection(); //Establish connection to Database
					
					PreparedStatement statement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){

				//Prepare SQL Statement
				conn.setAutoCommit(true);
				statement.setInt(1, user.getUserID());


				ResultSet returnedUser =statement.executeQuery();
				if(returnedUser.next()) {
					returnedUser.beforeFirst();//Ensure we start at first
					while(returnedUser.next()) {
						userIDOutputLbl.setText(returnedUser.getString("userID"));
						firstNameOutputLbl.setText(returnedUser.getString("firstName"));
						lastNameOutputLbl.setText(returnedUser.getString("lastName"));
						addressOutputLbl.setText(returnedUser.getString("address"));
						phoneNumberOutputLbl.setText(returnedUser.getString("phoneNumber"));
					}
				}

			} catch(SQLException e) {
				System.out.println("DB Error: " + e.getMessage());
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
