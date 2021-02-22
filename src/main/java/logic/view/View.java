package logic.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface View {
	Scene getScene();
	void setWindowProperties(Stage stage);
	void visible();
}
