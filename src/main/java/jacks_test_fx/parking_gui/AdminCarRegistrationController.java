package jacks_test_fx.parking_gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AdminCarRegistrationController {

    @FXML
    private ComboBox<String> userComboBox;
    public void initialize() {
		//Get DB cars change dtype to User rather than String
		userComboBox.getItems().addAll("Apple", "Banana", "Cherry");
    }

    @FXML
    private TextField make;

    @FXML
    private TextField model;

    @FXML
    private Button newUserButton;

    @FXML
    private TextField plate;

    @FXML
    private Button submit;

    @FXML
    private TextField year;

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

    }

}
