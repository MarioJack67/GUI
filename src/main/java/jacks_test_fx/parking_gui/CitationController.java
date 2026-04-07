package jacks_test_fx.parking_gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CitationController{

	
	
	@FXML
    private ComboBox<String> carComboBox;
	public void initialize() {
		carComboBox.getItems().addAll("Apple", "Banana", "Cherry");
	}
	
    @FXML
    private TextArea citationNotes;

    @FXML
    private Button newCarButton;

    @FXML
    private Button submitTicketButton;

    @FXML
    void addNewCar(ActionEvent event) {
    	System.out.println("New Car Button Pressed!");
    	try {
			MainApp.setRoot("registration","Parking Lot Managment System");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    void submitNewTicket(ActionEvent event) {
    	System.out.println("Submit Ticket Button Pressed!");
    }

}
