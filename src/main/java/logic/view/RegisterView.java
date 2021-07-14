package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.GraphicsController;
import logic.graphicscontroller.RegisterViewController;

public final class RegisterView implements ControllableView {
	private Scene scene;

	private static final String CHOOSE_MSG = "Choose one of the three options";
	private static final String JOB_SEEKER_BTN = "Register as a job seeker";
	private static final String COMPANY_BTN = "Register as a company";
	private static final String CONFIRM_BTN = "Confirm a pending registration";
	private static final int CONFIG_WIN_WIDTH = 265;
	private static final int CONFIG_WIN_HEIGHT = 300;

	private static final String WIN_TITLE = "Register - Whork";

	private Label chooseLabel;
	private Button jobSeekerBtn;
	private Button companyBtn;
	private Button confirmBtn;

	private GraphicsController controller;

	public RegisterView(ViewStack viewStack) {
		controller = new RegisterViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void populateScene() {
		VBox vbox = new VBox();
		vbox.getChildren().add(chooseLabel);
		vbox.getChildren().add(jobSeekerBtn);
		vbox.getChildren().add(companyBtn);
		vbox.getChildren().add(confirmBtn);

		scene = new Scene(vbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGHT);
	}

	private void setNodesProps() {
		//future usage
	}

	private void init() {
		chooseLabel = new Label(CHOOSE_MSG);
		jobSeekerBtn = new Button(JOB_SEEKER_BTN);
		companyBtn = new Button(COMPANY_BTN);
		confirmBtn = new Button(CONFIRM_BTN);
		controller.setup();
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setTitle(WIN_TITLE);
		stage.setResizable(false);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public Node[] getNodes() {
		return new Node[] { 
			jobSeekerBtn,
			companyBtn,
			confirmBtn
		};
	}

	@Override
	public void visible() {
		//no need to update anything
	}
}
