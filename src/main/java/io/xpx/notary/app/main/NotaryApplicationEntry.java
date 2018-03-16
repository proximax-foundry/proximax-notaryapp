package io.xpx.notary.app.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//	Entry point class
public class NotaryApplicationEntry extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
			Parent root;
			root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Run monitor tool
	}

	public static void main(String[] args) {
		launch(args);
	}

}
