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

	private void init() {
		logoutBtn = new Button("Logout");
		controller.setup();
	}

	private void setNodesProps() {
		//no nodes props for now
	}

	private void populateScene() {
		VBox vbox = new VBox();
		vbox.getChildren().add(logoutBtn);

		scene = new Scene(vbox, DefaultWindowSize.WIDTH, DefaultWindowSize.HEIGHT);
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
