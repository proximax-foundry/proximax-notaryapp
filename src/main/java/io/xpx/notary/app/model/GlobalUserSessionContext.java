package io.xpx.notary.app.model;

import java.nio.file.Paths;

import io.xpx.notary.app.utils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

public class GlobalUserSessionContext {
	
	private static String address;
	private static String privateKey;
	private static String recipientPublicKey = "8043f36622be5c91e00d9977c870935c887ff9050ba0a62207db76dba1a87385";
	private static String storageSource;

	private static ObservableList<String> unConfirmedAssets = FXCollections.observableArrayList();
	private static ObservableList<String> unConfirmedSentAssets = FXCollections.observableArrayList();
	private static ObservableList<String> unConfirmedReceivedAssets = FXCollections.observableArrayList();
	private static ObservableList<String> walletIdentities = FXCollections.observableArrayList();

	public static void initializeWalletList(ChoiceBox<String> walletList) {
		walletIdentities.clear();
		walletIdentities.add(0, "Select Wallet");
		FileUtils.recursiveFind(Paths.get("."), p -> {
			if (p.toFile().getName().toString().contains(".smlt")) {
				walletIdentities.add(p.getFileName().toString());
			}
		});
		walletList.setItems(walletIdentities);
		walletList.getSelectionModel().select(0);
	}

	public static String getStorageSource() {
		return storageSource;
	}

	public static void setStorageSource(String storageSource) {
		GlobalUserSessionContext.storageSource = storageSource;
	}

	public static ObservableList<String> getWalletIdentities() {
		return walletIdentities;
	}

	public static void setWalletIdentities(ObservableList<String> walletIdentities) {
		GlobalUserSessionContext.walletIdentities = walletIdentities;
	}

	public static String getRecipientPublicKey() {
		return recipientPublicKey;
	}

	public static void setRecipientPublicKey(String recipientPublicKey) {
		GlobalUserSessionContext.recipientPublicKey = recipientPublicKey;
	}

	public static String getAddress() {
		address = address.replace("-", "");
		return address;
	}

	public static void setAddress(String address) {
		GlobalUserSessionContext.address = address;
	}

	public static String getPrivateKey() {
		return privateKey;
	}

	public static void setPrivateKey(String privateKey) {
		GlobalUserSessionContext.privateKey = privateKey;
	}

	public static ObservableList<String> getUnConfirmedAssets() {
		return unConfirmedAssets;
	}

	public static void setUnConfirmedAssets(ObservableList<String> unConfirmedAssets) {
		GlobalUserSessionContext.unConfirmedAssets = unConfirmedAssets;
	}

	public static ObservableList<String> getUnConfirmedSentAssets() {
		return unConfirmedSentAssets;
	}

	public static void setUnConfirmedSentAssets(ObservableList<String> unConfirmedSentAssets) {
		GlobalUserSessionContext.unConfirmedSentAssets = unConfirmedSentAssets;
	}

	public static ObservableList<String> getUnConfirmedReceivedAssets() {
		return unConfirmedReceivedAssets;
	}

	public static void setUnConfirmedReceivedAssets(ObservableList<String> unConfirmedReceivedAssets) {
		GlobalUserSessionContext.unConfirmedReceivedAssets = unConfirmedReceivedAssets;
	}

	public static void loadWalletsOrIdentities() {

	}
}
