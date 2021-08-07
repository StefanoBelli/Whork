package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.graphicscontroller.GraphicsController;
import logic.graphicscontroller.PasswordChangeViewController;
import logic.util.GraphicsUtil;

public final class PasswordChangeView implements ControllableView {

	private static final String WIN_TITLE = "Whork - Change password";
	private static final int CONFIG_WIN_WIDTH = 265;
	private static final int CONFIG_WIN_HEIGHT = 260;
	private static final String SAY_TOKEN = "Token:";
	private static final String SAY_PASSWORD = "New password:";
	private static final String SAY_RETYPE_PASSWORD = "Retype password:";
	private static final String BTN_CONTINUE = "Continue";
	private static final String BTN_GOBACK = "Go back";
	private static final String PASSWORD_PROMPT = "Enter your new password...";
	private static final String TOKEN_PROMPT = "Enter your token...";
	private static final String RETYPE_PASSWORD_PROMPT = "Retype your new password...";
	private static final String FINAL_PHASE = 
		"Password recovery final phase";
	
	private Scene scene;

	private Text finalPhaseText;
	private Label tokenLabel;
	private TextField tokenTextField;
	private Label passwordLabel;
	private TextField passwordTextField;
	private Label retypePasswordLabel;
	private TextField retypePasswordTextField;
	private Button continueButton;
	private Button goBackButton;

	private GraphicsController controller;

	public PasswordChangeView(ViewStack viewStack) {
		controller = new PasswordChangeViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		finalPhaseText = new Text(FINAL_PHASE);
		tokenLabel = new Label(SAY_TOKEN);
		tokenTextField = new TextField();
		passwordLabel = new Label(SAY_PASSWORD);
		passwordTextField = new PasswordField();
		retypePasswordLabel = new Label(SAY_RETYPE_PASSWORD);
		retypePasswordTextField = new PasswordField();
		continueButton = new Button(BTN_CONTINUE);
		goBackButton = new Button(BTN_GOBACK);
		controller.setup();
	}

	private void setNodesProps() {
		tokenTextField.setPromptText(TOKEN_PROMPT);
		passwordTextField.setPromptText(PASSWORD_PROMPT);
		retypePasswordTextField.setPromptText(RETYPE_PASSWORD_PROMPT);
		continueButton.setDisable(true);
		finalPhaseText.setFont(GraphicsUtil.getBoldFont());
	}

	private void populateScene() {
		VBox vbox = new VBox(10);
		vbox.getChildren().add(finalPhaseText);
		vbox.getChildren().add(tokenLabel);
		vbox.getChildren().add(tokenTextField);
		vbox.getChildren().add(passwordLabel);
		vbox.getChildren().add(passwordTextField);
		vbox.getChildren().add(retypePasswordLabel);
		vbox.getChildren().add(retypePasswordTextField);

		HBox btnsHbox = new HBox(5);
		btnsHbox.getChildren().add(continueButton);
		btnsHbox.getChildren().add(goBackButton);

		vbox.getChildren().add(btnsHbox);

		scene = new Scene(vbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGHT);
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
			tokenTextField,
			passwordTextField,
			retypePasswordTextField,
			continueButton,
			goBackButton
		};
	}

	@Override
	public void visible() {
		// no need to update anything
	}
}
