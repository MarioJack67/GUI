package jacks_test_fx.parking_gui;
/*
Put header here


 */

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class FXMLController implements Initializable {
    
    
    @FXML
    private void openRegistration(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
        Parent parent = loader.load();
        RegistrationController rControl = loader.getController();
        
        Stage stage = new Stage();
        stage.setTitle("Register your Car");
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.WINDOW_MODAL);
        
        Window owner = ((Button) event.getSource()).getScene().getWindow();
        stage.initOwner(owner);
        stage.showAndWait();
        
        String enteredModel = rControl.getModel();
        System.out.println(enteredModel);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
