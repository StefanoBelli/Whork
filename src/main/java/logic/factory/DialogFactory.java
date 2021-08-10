package logic.factory;

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public final class DialogFactory {
	private DialogFactory() {}

	private static Alert buildDialog(AlertType type, String title, String headerText, String contentText) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		return alert;
	}

	public static Alert error(String title, String headerText, String contentText) {
		return buildDialog(AlertType.ERROR, title, headerText, contentText);
	}

	public static Alert info(String title, String headerText, String contentText) {
		return buildDialog(AlertType.INFORMATION, title, headerText, contentText);
	}

	public static Dialog<String> input(String title, String headerText, String contentText) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);

		return dialog;
	}
}
