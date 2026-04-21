package jacks_test_fx.parking_gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtility {

	
	/**
	 * 
	 * @param <T>
	 * @param event the ActionEvent tied to scene switch
	 * @param fxmlName fxml scene name such as primary or citation
	 * @return
	 * @throws IOException
	 */
	public static <T> T switchScene(ActionEvent event, String fxmlName) throws IOException {
	    String fxmlPath = "/fxml/" + fxmlName + ".fxml";
	    FXMLLoader loader = new FXMLLoader(SceneUtility.class.getResource(fxmlPath));
	    Parent root = loader.load();
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(new Scene(root));
	    return loader.getController();
	}
}
