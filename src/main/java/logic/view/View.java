package logic.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Stefano Belli
 */
public interface View {
	Scene getScene();
	void setWindowProperties(Stage stage);
	void visible();
}
