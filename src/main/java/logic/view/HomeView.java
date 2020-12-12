package logic.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;

public final class HomeView {
	private final Scene mainScene;
	
	public HomeView() {
		Label label = new Label("Whork desktop app");
		label.setId("mainLabel");
		HBox hbox = new HBox(label);
		this.mainScene = new Scene(hbox);
	}
	
	public Scene getScene() {
		return this.mainScene;
	}

}
