package logic;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.view.HomeView;

public final class WhorkDesktop extends Application {
	
	private final HomeView view = new HomeView();
	
	public static void launchApp(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setScene(view.getScene());
		primaryStage.setTitle("Whork");
		primaryStage.show();
	}
}

