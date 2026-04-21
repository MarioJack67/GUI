package jacks_test_fx.parking_gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CitationController implements Initializable {

	//IDK Maybe do something w/ this
//	private ParkingCitation currentCitation;
	
	@FXML
    private ComboBox<Car> carComboBox;
	public void initialize(URL location, ResourceBundle resources) {
		//Get DB cars change dtype to Car rather than String
//		carComboBox.getItems().addAll("Apple", "Banana", "Cherry");
		setSelection();
	}
	
	@FXML
    public void setSelection() {
    	ObservableList<Car> selections = FXCollections.observableArrayList(fetchCars());
    	carComboBox.setItems(selections);
    }
	
    @FXML
    private TextArea citationNotes;

    @FXML
    private Button newCarButton;

    @FXML
    private Button submitTicketButton;
    
    @FXML
    private Label submitWarn;

    @FXML
    void addNewCar(ActionEvent event) {
    	System.out.println("New Car Button Pressed!");
    	try {
			MainApp.switchRoot("adminCarRegistration");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    void submitNewTicket(ActionEvent event) throws IOException{
    	submitWarn.setText("");
    	System.out.println("Submit Ticket Button Pressed!");
    	updateCitationTable();
    }
    
private void updateCitationTable() throws IOException{
		
		Car selectedCar = carComboBox.getValue();
		
		if (selectedCar == null) {
	        submitWarn.setText("* Car Field Required");
	        return;
	    }
		
		
		ParkingCitation currentCitation = new ParkingCitation(selectedCar, citationNotes.getText());
		
		
		
		int selectedCarID = getCurrentCarID(selectedCar);
		//Exits if can't find car ID to prevent DB issues
		if(selectedCarID == -1) { return; }
		
		
		//==========================================================================================================
		
		//update citation table in DB
		String sql = "INSERT INTO Citations (fee, citationDate, paymentDate, citationNote, carID) VALUES (?, ?, ?, ?, ?);";
		try(Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql)){
				conn.setAutoCommit(false);
				
				statement.setDouble(1, currentCitation.getFeeAmount());
				statement.setDate(2, Date.valueOf(currentCitation.getCitationDate()));
				statement.setDate(3, Date.valueOf(currentCitation.getPaymentDeadline()));
				statement.setString(4, currentCitation.getNotes());
				statement.setInt(5, selectedCarID);
				statement.executeUpdate();
				
				conn.commit();
				MainApp.switchRoot("primary");
			} catch(SQLException e) {
				System.out.println("DB Error: " + e.getMessage());
			}
	}

	private int getCurrentCarID(Car selectedCar) {
		//find carID from car table in DB
		String sql = "SELECT carID FROM Cars WHERE make = ? AND model = ? AND year = ? AND plate = ?";
		try(Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql)){
				conn.setAutoCommit(false);
					
				statement.setString(1, selectedCar.getMake());
				statement.setString(2, selectedCar.getModel());
				statement.setString(3, selectedCar.getYear());
				statement.setString(4, selectedCar.getPlate());
				ResultSet results = statement.executeQuery();
				if(results.next()) {
//					conn.commit();
					return results.getInt("carID");
				}
				else {
					System.out.println("Error, Car object not found in DB!");
					return -1;
				}			
			} catch(SQLException e) {
				System.out.println("DB Error: " + e.getMessage());
			}
		return -1;
	}

    private List<Car> fetchCars() {
    	List<Car> carsList = new ArrayList<>();
    	String sql = "SELECT c.make, c.model, c.year, c.plate, u.userID, u.firstName, u.lastName, u.address, u.phoneNumber, u.accessLevel FROM Cars c JOIN Users u ON c.userID = u.userID";
    	try(Connection conn = DBConnection.getConnection();
    			PreparedStatement statement = conn.prepareStatement(sql)){	
    			conn.setAutoCommit(true);
    			
    			ResultSet results = statement.executeQuery();
    			while(results.next()) {
    				// Car fields
    	            String make = results.getString("make");
    	            String model = results.getString("model");
    	            String year = results.getString("year");
    	            String plate = results.getString("plate");

    	            // User fields
    	            int userID = results.getInt("userID");
    	            String fname = results.getString("firstName");
    	            String lname = results.getString("lastName");
    	            String address = results.getString("address");
    	            String phone = results.getString("phoneNumber");
    	            int accessLevel = results.getInt("accessLevel");
    	            
    	            User currentCarUser = new User(userID, fname, lname, address, phone, accessLevel);
    				Car currentCar = new Car(make, model, year, plate, currentCarUser);
    				carsList.add(currentCar);
    			}
    		} catch(SQLException e) {
    			System.out.println("DB Error: " + e.getMessage());
    		}
    	return carsList;
    }
    
    private Stage stage;
	private User user;
    
    /**
	 * Used to provide a closing method once this window has completed its role
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setUser(User user) {
		user = this.user;
	}

}
