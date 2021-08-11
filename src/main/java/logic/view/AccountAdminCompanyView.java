package logic.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.AccountAdminCompanyViewController;
import logic.graphicscontroller.GraphicsController;

public class AccountAdminCompanyView implements ControllableView {
	private static final double WIDTHWINDOW = DefaultWindowSize.WIDTH+400;
	private static final double HEIGHTWINDOW = DefaultWindowSize.HEIGHT+150;

	private static final String COMMON_STYLING =
		"-fx-border-style: solid;-fx-border-width: 1;-fx-border-color: black";
	
	private Button homeBtn;
	private Button postOfferBtn;
	private Button logOutBtn;

	private Scene scene;

	private GraphicsController controller;

	public AccountAdminCompanyView(ViewStack viewStack) {
		controller = new AccountAdminCompanyViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		homeBtn = new Button("Home");
		postOfferBtn = new Button("Post Offer");
		logOutBtn = new Button("Log Out");
		
		controller.setup();
	}

	private void setNodesProps() {
		
	}
	
	private void populateScene() {
		VBox vbox = new VBox();

		VBox vboxPannel = new VBox();
		vboxPannel.getChildren().add(homeBtn);
		vboxPannel.getChildren().add(postOfferBtn);
		vboxPannel.getChildren().add(logOutBtn);
		vboxPannel.setPadding(new Insets(10, 10, 10, 10));
		vboxPannel.setStyle(COMMON_STYLING);
		
		vbox.getChildren().add(vboxPannel);
		scene = new Scene(vbox, WIDTHWINDOW, HEIGHTWINDOW);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setResizable(false);
		stage.setTitle("My Account - Whork");
	}

	@Override
	public void visible() {
		//no need to call update
	}

	@Override
	public Node[] getNodes() {
		return new Node[] {
			homeBtn,
			postOfferBtn,
			logOutBtn
		};
	}
}






