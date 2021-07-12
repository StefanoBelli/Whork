package logic.util;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.view.ExceptionView;
import logic.view.ViewStack;

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
		Stage s = (Stage) ((Button) event.getSource()).getScene().getWindow();
		s.close();
	}

	public static Font getBoldFont() {
		Font dflFont = Font.getDefault();
		return Font.font(dflFont.getFamily(), FontWeight.BOLD, dflFont.getSize());
	}
}