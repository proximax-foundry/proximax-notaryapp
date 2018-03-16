package io.xpx.notary.app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import io.xpx.notary.app.model.GlobalUserSessionContext;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class RegistrationController extends AbstractController implements Initializable {
	@FXML
	private Pane ap;
	@FXML
	private TextField identityName;
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField confirmPassword;

	@FXML
	private ChoiceBox<String> networkChoice;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//networkChoice.setItems(FXCollections.observableArrayList("Select Network", "testnet"));
		//networkChoice.getSelectionModel().select(0);
	}

	public void create(Event event) {
		GlobalUserSessionContext.getWalletIdentities().add(identityName.getText() + ".ntap");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Registration Success");
		alert.setContentText("Credentials generated. Please keep your .smlt file");
		alert.show();

		ap.getScene().getWindow().hide();
	}

}
