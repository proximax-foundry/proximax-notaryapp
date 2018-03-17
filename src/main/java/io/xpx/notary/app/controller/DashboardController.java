package io.xpx.notary.app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class DashboardController extends AbstractController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void goToNotaryRegistration(Event event) {
		try {


			// load
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NotaryRegistration.fxml"));
			Parent root;
			root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();

			// hide
			//loginPane.getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
