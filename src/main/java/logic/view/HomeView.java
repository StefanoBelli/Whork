package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.GraphicsController;
import logic.graphicscontroller.HomeViewController;

public final class HomeView implements ControllableView {
	private static final String CONFIG_WIN_TITLE = "Whork - Find a job";

	private Button accountBtn;

	private GraphicsController controller;

	private Scene scene;

	public HomeView(ViewStack viewStack) {
		controller = new HomeViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		accountBtn = new Button();
		controller.setup();
	}

	private void setNodesProps() {
		//no need to set node props for now
	}

	private void populateScene() {
		VBox vbox = new VBox();
		vbox.getChildren().add(accountBtn);
		
		scene = new Scene(vbox, WindowSize.WIDTH, WindowSize.HEIGHT);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setResizable(false);
		stage.setTitle(CONFIG_WIN_TITLE);
	}
	
	@Override
	public Node[] getNodes() {
		return new Node[] {
			accountBtn
		};
	}

	@Override
	public void visible() {
		controller.update();
	}
}
