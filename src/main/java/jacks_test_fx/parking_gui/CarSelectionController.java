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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CarSelectionController implements Initializable {
	
	private ParkingSpot currentSpot;
	private User currentUser;

    @FXML
    private ComboBox<Car> carComboBox;

    @FXML
    private Button newCarButton;

    @FXML
    private AnchorPane registerButton;

    @FXML
    private Button submitTicketButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    }
    
    @FXML
    public void setSelection() {
    	ObservableList<Car> selections = FXCollections.observableArrayList(fetchCars(currentUser.getUserID()));
    	carComboBox.setItems(selections);
    }

    @FXML
    void addNewCar(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/carRegistration.fxml"));
        Parent parent = loader.load();
        CarRegistrationController rControl = loader.getController();
        rControl.readUser(currentUser);
        rControl.readCars(carComboBox.getItems());
        
        Stage stage = new Stage();
        stage.setTitle("Register your Car");
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.WINDOW_MODAL);
        
        Window owner = ((Button) event.getSource()).getScene().getWindow();
        stage.initOwner(owner);
        stage.showAndWait();
    }

    @FXML
    void submitNewTicket(ActionEvent event) {
    	currentSpot.setTaken(true);
    	Stage stage = (Stage) submitTicketButton.getScene().getWindow();
		stage.close();
    }
    
    public void readParkingSpot(ParkingSpot currentSpot) {
		this.currentSpot = currentSpot;
	}
    
    public void readUser(User user) {
    	this.currentUser = user;
    }
    
    private List<Car> fetchCars(int userID) {
    	List<Car> userCars = new ArrayList<>();
    	String sql = "SELECT make, model, year, plate FROM Cars WHERE userID = ?";
    	try(Connection conn = DBConnection.getConnection();
    			PreparedStatement statement = conn.prepareStatement(sql)){	
    			conn.setAutoCommit(true);
    			
    			statement.setInt(1, userID);
    			ResultSet results = statement.executeQuery();
    			while(results.next()) {
    				String make = results.getString("make");
    				String model = results.getString("model");
    				String year = results.getString("year");
    				String plate = results.getString("plate");
    				Car currentCar = new Car(make, model, year, plate, currentUser);
    				userCars.add(currentCar);
    			}
    		} catch(SQLException e) {
    			System.out.println("DB Error: " + e.getMessage());
    		}
    	return userCars;
    }

}
