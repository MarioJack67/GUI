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

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.model.CustomerSearchResult;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.stripe.model.PaymentIntentSearchResult;
import com.stripe.model.checkout.SessionCollection;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.PaymentIntentListParams;
import com.stripe.param.PaymentIntentRetrieveParams;
import com.stripe.param.PaymentIntentSearchParams;
import com.stripe.param.checkout.SessionListParams;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CarSelectionController implements Initializable {
	
	private ParkingSpot currentSpot;
	private User currentUser;
	private HostServices hostServices = jacks_test_fx.parking_gui.Session.host;

    @FXML
    private ComboBox<Car> carComboBox;

    @FXML
    private Button newCarButton;

    @FXML
    private AnchorPane registerButton;

    @FXML
    private Button submitTicketButton;
    
    @FXML
    private Button paidButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//    	Session.parkingSpot = currentSpot;
    	setSelection();
    }
    
    @FXML
    public void setSelection() {
    	ObservableList<Car> selections = FXCollections.observableArrayList(fetchCars(jacks_test_fx.parking_gui.Session.currentUser.getUserID()));
    	carComboBox.setItems(selections);
    }

    @FXML
    void addNewCar(ActionEvent event) throws IOException {
    	System.out.println("Add Car Button Pressed!");
    	jacks_test_fx.parking_gui.Session.cars = carComboBox.getItems();
    	SceneUtility.switchScene(event, "carRegistration", "Register Your Car");
    }

    @FXML
    void submitNewTicket(ActionEvent event) {
    	if(hostServices != null) {
    		//Goes to Stripe Test Payment Processor
    		hostServices.showDocument("https://buy.stripe.com/test_bJe28qgYQ3zgbA05A89Ve00");
    		submitTicketButton.setVisible(false);
    		paidButton.setVisible(true);
    	} else {
    		System.out.println("Stripe Not Initialized");
    	}
    }
    
    @FXML
    /**
     * Checks the session on Stripe to see if customer with matching phone number has paid
     * Refunds that are issued will remove the user from the paid session, making them pay again
     * 
     * @author Jack B
     */
    private void checkPayment() {
    	String firstPart = "sk_test_51TOozYCNxPB6776Zc6YD6aeVhlHFda1r8qyGBLcV";
    	String secondPart = "x0n6rb5XEz9rqRx4LKEhFkvHqktjOPjjbu6kFWgKDjVeTeI900dvqL8AEM";
    	Stripe.apiKey = firstPart + secondPart;
    	String userPhone = formatToE164(jacks_test_fx.parking_gui.Session.currentUser.getPhoneNum().trim());
    	try {
    		// Search Checkout Sessions (the most recent ones)
            SessionListParams params = SessionListParams.builder()
                .setLimit(10L) // Check the last 10 sessions
                .build();

            SessionCollection sessions = Session.list(params);

            boolean hasPaid = false;
    			
            for (Session session : sessions.getData()) {
                // Check if the session is paid and the phone matches
                if ("complete".equals(session.getStatus()) && 
                    "paid".equals(session.getPaymentStatus()) &&
                    session.getCustomerDetails() != null &&
                    userPhone.equals(session.getCustomerDetails().getPhone())) {
                	
                	PaymentIntentRetrieveParams retrieveParams = PaymentIntentRetrieveParams.builder()
                		    .addExpand("latest_charge")
                		    .build();

                		PaymentIntent intent = PaymentIntent.retrieve(session.getPaymentIntent(), retrieveParams, null);

                		// Access the Charge object from the expanded field
                		Charge latestCharge = intent.getLatestChargeObject();

                		if (latestCharge != null) {
                		    // Check if the charge was fully refunded or has a partial refund amount
                		    if (latestCharge.getRefunded() || (latestCharge.getAmountRefunded() != null && latestCharge.getAmountRefunded() > 0)) {
                		        break; 
                		    }
                		}
                	
                    hasPaid = true;
                    break;
                }
            }
            
            if(hasPaid) {
            	//Register car and close screen
            	System.out.println("Session.parkingSpot = " + jacks_test_fx.parking_gui.Session.parkingSpot);
    	    	jacks_test_fx.parking_gui.Session.parkingSpot.setTaken(true);
    	    	Stage stage = (Stage) paidButton.getScene().getWindow();
    			stage.close();
            } else {
            	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        	    alert.setTitle("Error");
        	    alert.setHeaderText(null); // optional — removes the header
        	    alert.setContentText("No Payment Found.");
        	    alert.showAndWait();
            }
    	} catch(StripeException e) {
    		System.out.println(e.getMessage());
    	}
    }
    
    public void readParkingSpot(ParkingSpot currentSpot) {
		this.currentSpot = currentSpot;
	}
    
    public void readUser(User user) {
    	this.currentUser = user;
    }
    
    /**
     * Retrieves all records of cars associated with the current user and returns them in a list of Car objects
     * @param userID - Which user is getting their cars checked?
     * @return - A list of all cars associated with user, packaged into Car objects
     * 
     * @author Jack B
     */
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
    				Car currentCar = new Car(make, model, year, plate, jacks_test_fx.parking_gui.Session.currentUser);
    				userCars.add(currentCar);
    			}
    		} catch(SQLException e) {
    			System.out.println("DB Error: " + e.getMessage());
    		}
    	return userCars;
    }
    
    /**
     * Formats the phone number to match Stripe output and match customer to payment
     * @param phoneNumber - Phone number of current user
     * @return - Phone number in format that matches Stripe
     * 
     * @author Jack B
     */
    private String formatToE164(String phoneNumber) {
        // Strip all non-numeric characters except the '+'
        String digits = phoneNumber.replaceAll("[^\\d]", "");
        
        // If user provided exactly 10 digits (US/Canada), prepend +1
        if (digits.length() == 10) {
            return "+1" + digits;
        } 
        // If they provided 11 digits starting with 1, just add the +
        else if (digits.length() == 11 && digits.startsWith("1")) {
            return "+" + digits;
        }
        
        // Otherwise, ensure there is at least a leading '+'
        return phoneNumber.startsWith("+") ? phoneNumber : "+" + digits;
    }
}
