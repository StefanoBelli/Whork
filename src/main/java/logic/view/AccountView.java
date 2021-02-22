package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.AccountViewController;
import logic.graphicscontroller.GraphicsController;

public final class AccountView implements ControllableView {

	private Scene scene;

	private Button logoutBtn;

	private GraphicsController controller;

	public AccountView(ViewStack viewStack) {
		controller = new AccountViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	public void init() {
		logoutBtn = new Button("Logout");
		controller.setup();
	}

	public void setNodesProps() {
		//no nodes props for now
	}

	public void populateScene() {
		VBox vbox = new VBox();
		vbox.getChildren().add(logoutBtn);

		scene = new Scene(vbox, WindowSize.WIDTH, WindowSize.HEIGHT);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setTitle("Whork - My Account");
	}

	@Override
	public void visible() {
		//no need to call update
	}

	@Override
	public Node[] getNodes() {
		return new Node[] {
			logoutBtn
		};
	}
	
}
