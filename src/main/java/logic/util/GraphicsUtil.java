package logic.util;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.view.ExceptionView;
import logic.view.ViewStack;
import javafx.stage.FileChooser.ExtensionFilter;

public final class GraphicsUtil {
	private GraphicsUtil() {}

	public static void showExceptionStage(Exception e) {
		Stage newStage = new Stage();
		ViewStack stack = new ViewStack(newStage);
		stack.push(new ExceptionView(e, stack));
		newStage.setOnCloseRequest(ev -> Platform.exit());
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.show();
	}

	public static void closeStageByMouseEvent(MouseEvent event) {
		((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
	}

	public static Font getBoldFont() {
		Font dflFont = Font.getDefault();
		return Font.font(dflFont.getFamily(), FontWeight.BOLD, dflFont.getSize());
	}

	public static File showFileChooser(Stage owner, String title, ExtensionFilter ...filters) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		fileChooser.getExtensionFilters().addAll(filters);

		return fileChooser.showOpenDialog(owner);
	}

	public static final class OnlyNumbersChangeListener implements ChangeListener<String> {
		private final TextField textField;
		
		public OnlyNumbersChangeListener(TextField textField) {
			this.textField = textField;
		}

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, 
			String newValue) {
			if (!newValue.matches("\\d*")) {
				textField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		}
	}
}