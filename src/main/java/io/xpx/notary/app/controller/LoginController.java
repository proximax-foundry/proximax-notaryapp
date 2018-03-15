package io.xpx.notary.app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController extends AbstractController implements Initializable {
	
	@FXML
	private Pane loginPane;
	@FXML
	private TextField addressField;
	@FXML
	private PasswordField passwordField;

	private Alert alert;

	public void login(ActionEvent event) {
		try {
			// validate
			try {
				runMonitorTool();
			} catch (Exception e) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error logging in");
				alert.setContentText("Invalid Identity combination");
				alert.show();
				e.printStackTrace();
				return;
			}

			// load
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
			Parent root;
			root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();

			// hide
			loginPane.getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void signUp(Event event) {
		// loginPane.getScene().getWindow().hide();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Registration.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();

	}

	private void runMonitorTool() throws Exception {

		// // WebSockets listener for NEM
		// WsNemTransactionMonitor.networkName(credentials.getNemNetwork().toLowerCase())
		// .host("104.128.226.60")
		// .port("7890")
		// .wsPort("7778")
		// .addressesToMonitor(UserSessionContext.getAddress())
		// .subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS,
		// new NemTransactionHandler(UserSessionContext.getAddress()))
		// .monitor();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
