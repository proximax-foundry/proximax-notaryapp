package io.xpx.notary.app.controller;

import java.text.SimpleDateFormat;

import javafx.scene.layout.Pane;

public abstract class AbstractController {
	protected SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
	protected Pane pane;

	protected void setPane(Pane pane) {
		this.pane = pane;
	}

	protected void hidePane() {
		this.pane.getScene().getWindow().hide();
	}

}
