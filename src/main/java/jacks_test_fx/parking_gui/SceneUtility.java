package jacks_test_fx.parking_gui;

import java.io.IOException;
import java.util.Stack;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SceneUtility {

	public static Stack<String> previousStages = new Stack<>();
	
	/**
	 * Helper function to switch scenes within a window.
	 * 
	 * @param <T>
	 * @param event the ActionEvent tied to scene switch
	 * @param fxmlName fxml scene name such as primary or citation
	 * @param title new title of the window
	 * @return
	 * @throws IOException
	 */
	public static <T> T switchScene(ActionEvent event, String fxmlName, String title) throws IOException {
		if(previousStages.size() > 0) {
			//If page history is preserved in stack prioritizes previous scene over the given scene
			fxmlName = previousStages.pop();
    	}
		System.out.println("Switching to [" + fxmlName + "] scene!");
	    FXMLLoader loader = new FXMLLoader(SceneUtility.class.getResource("/fxml/" + fxmlName + ".fxml"));
	    Parent root = loader.load();
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(new Scene(root));
	    stage.setTitle(title);
	    return loader.getController();
	}
	
	/**
	 * Helper function to switch scenes within a window.
	 * 
	 * @param <T>
	 * @param event the ActionEvent tied to scene switch
	 * @param fxmlName fxml scene name such as primary or citation
	 * @param controllerInit Allows us to pass data to the next scene easily
	 * @param title new title of the window
	 * @return
	 * @throws IOException
	 */
	public static <T> T switchScene(ActionEvent event, String fxmlName, String title, Consumer<T> controllerInit) throws IOException {
		if(previousStages.size() > 0) {
			//If page history is preserved in stack prioritizes previous scene over the given scene
			fxmlName = previousStages.pop();
    	}
		System.out.println("Switching to [" + fxmlName + "] scene!");
		FXMLLoader loader = new FXMLLoader(SceneUtility.class.getResource("/fxml/" + fxmlName + ".fxml"));
	    Parent root = loader.load();
	    T controller = loader.getController();
	    if (controllerInit != null) {
	        controllerInit.accept(controller);
	    }
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(new Scene(root));
	    stage.setTitle(title);
	    return controller;
	}
	
	
	/**
	 * Helper function to create a pop-out window.
	 * 
	 * @param <T>
	 * @param event the ActionEvent tied to scene switch
	 * @param fxmlName fxml scene name such as primary or citation
	 * @param title new title of the window
	 * @return
	 * @throws IOException
	 */
	public static <T> T popoutScene(ActionEvent event, String fxmlName, String title) throws IOException {

	    if (previousStages.size() > 0) {
	        fxmlName = previousStages.pop();
	    }

	    System.out.println("Opening window [" + fxmlName + "]!");
	    FXMLLoader loader = new FXMLLoader(SceneUtility.class.getResource("/fxml/" + fxmlName + ".fxml"));
	    Parent root = loader.load();
	    T controller = loader.getController();
	    Stage stage = new Stage();
	    stage.setTitle(title);
	    stage.setMinWidth(MainApp.MIN_WIDTH);
        stage.setMinHeight(MainApp.MIN_HEIGHT);
	    stage.setScene(new Scene(root));

	    Stage owner = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.initOwner(owner);
	    stage.initModality(Modality.WINDOW_MODAL);
	    stage.showAndWait();

	    return controller;
	}
}
