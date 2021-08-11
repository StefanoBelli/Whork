package logic.view;

import java.util.Date;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.graphicscontroller.AccountCompanyViewController;
import logic.graphicscontroller.GraphicsController;

public class AccountCompanyView implements ControllableView {
	private static final double WIDTHWINDOW = DefaultWindowSize.WIDTH+400;
	private static final double HEIGHTWINDOW = DefaultWindowSize.HEIGHT+150;

	private static final String COMMON_STYLING =
		"-fx-border-style: solid;-fx-border-width: 1;-fx-border-color: black";
	
	private Button homeBtn;
	private Button postOfferBtn;
	private Button logOutBtn;
	private Text nameAdminText;
	private Text nameCompanyText;
	private Text dashboardText;
	private ImageView imgAdminView;
	private Text dateText;

	private Scene scene;

	private GraphicsController controller;

	public AccountCompanyView(ViewStack viewStack) {
		controller = new AccountCompanyViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		homeBtn = new Button("Home");
		postOfferBtn = new Button("Post Offer");
		logOutBtn = new Button("Log Out");
		nameAdminText = new Text();
		nameCompanyText = new Text();
		dashboardText = new Text("Dashboard");
		imgAdminView = new ImageView();
		dateText = new Text(new Date().toString().substring(0, 10));

		controller.setup();
	}

	private void setNodesProps() {
		imgAdminView.prefWidth(20);
	}
	
	private void populateScene() {
		VBox vbox = new VBox();

		VBox vboxPannel = new VBox(15);
		vboxPannel.getChildren().add(homeBtn);
		vboxPannel.getChildren().add(postOfferBtn);
		vboxPannel.getChildren().add(logOutBtn);
		vboxPannel.setPrefWidth(200);
		vboxPannel.setPadding(new Insets(10, 10, 10, 10));
		vboxPannel.setStyle(COMMON_STYLING);
		
		HBox hboxHeader = new HBox(10);
		//hboxHeader.setPrefWidth(200);
		VBox vboxHeader = new VBox(10);
		vboxHeader.getChildren().add(nameAdminText);
		vboxHeader.getChildren().add(nameCompanyText);
		vboxHeader.getChildren().add(dashboardText);
		hboxHeader.getChildren().add(vboxHeader);
		hboxHeader.getChildren().add(imgAdminView);
		hboxHeader.getChildren().add(dateText);

		hboxHeader.setAlignment(Pos.CENTER);
		hboxHeader.setStyle(COMMON_STYLING);
		hboxHeader.setPadding(new Insets(10, 10, 10, 10));
		vboxHeader.setPadding(new Insets(10, 400, 10, 10));
		//vboxHeader.setPrefWidth(500);


		HBox hboxHeaderPannel = new HBox();
		hboxHeaderPannel.getChildren().add(vboxPannel);
		hboxHeaderPannel.getChildren().add(hboxHeader);
		
		vbox.getChildren().add(hboxHeaderPannel);
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
			logOutBtn,
			nameAdminText,
			nameCompanyText,
			imgAdminView
		};
	}
}






