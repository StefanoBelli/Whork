package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.GraphicsController;
import logic.graphicscontroller.PasswordRecoveryViewController;

public final class PasswordRecoveryView implements ControllableView {

	private static final String WIN_TITLE = "Whork - Password recovery";
	private static final String BTN_MSG_SENDREQ = "Recover my password";
	private static final String BTN_MSG_GOTTOKEN = "I already have a token";
	private static final int CONFIG_WIN_WIDTH = 265;
	private static final int CONFIG_WIN_HEIGHT = 300;
	private static final String EMAIL_PROMPT_MSG = "Email address...";

	private Scene scene;

	private TextField emailAddressTextField;
	private Button sendRequestButton;
	private Button alreadyHaveTokenButton;

	private GraphicsController controller;

	public PasswordRecoveryView(ViewStack viewStack) {
		controller = new PasswordRecoveryViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		emailAddressTextField = new TextField();
		sendRequestButton = new Button(BTN_MSG_SENDREQ);
		alreadyHaveTokenButton = new Button(BTN_MSG_GOTTOKEN);
		controller.setup();
	}

	private void setNodesProps() {
		emailAddressTextField.setPromptText(EMAIL_PROMPT_MSG);
	}

	private void populateScene() {
		VBox vbox = new VBox();
		vbox.getChildren().add(emailAddressTextField);
		vbox.getChildren().add(sendRequestButton);
		vbox.getChildren().add(alreadyHaveTokenButton);
		scene = new Scene(vbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGHT);
	}
	
	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setTitle(WIN_TITLE);
		stage.setResizable(false);
	}

	@Override
	public void visible() {
		// no need to update anything
	}

	@Override
	public Node[] getNodes() {
		return new Node[] {
			emailAddressTextField,
			sendRequestButton,
			alreadyHaveTokenButton
		};
	}

	public void setPresetEmail(String emailAddress) {
		emailAddressTextField.setText(emailAddress);
	}
}
