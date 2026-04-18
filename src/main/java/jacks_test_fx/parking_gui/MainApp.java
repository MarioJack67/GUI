package jacks_test_fx.parking_gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;


public class MainApp extends Application {
    private static Stage stage;
    public static Stack<String> previousStages = new Stack<>(); 

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        stage=s;
        stage.setMinWidth(650);
        stage.setMinHeight(550);
        setRoot("citation","Parking Lot Managment System");
    }

    static void setRoot(String fxml) throws IOException {
        setRoot(fxml,stage.getTitle());
    }
    
    static void switchRoot(String fxml) throws IOException {
    	if(previousStages.size() > 0) {
    		setRoot(previousStages.pop(),stage.getTitle());
    	}
    	else {
    		setRoot(fxml,stage.getTitle());
    	}
    }

    static void setRoot(String fxml, String title) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
