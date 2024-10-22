package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.graphicscontroller.GraphicsController;
import logic.graphicscontroller.PasswordRecoveryViewController;
import logic.util.GraphicsUtil;

/**
 * @author Stefano Belli
 */
public final class PasswordRecoveryView implements ControllableView {

	private static final String WIN_TITLE = "Whork - Password recovery";
	private static final String BTN_MSG_SENDREQ = "Recover my password";
	private static final String BTN_MSG_GOTTOKEN = "I already have a token";
	private static final int CONFIG_WIN_WIDTH = 265;
	private static final int CONFIG_WIN_HEIGHT = 260;
	private static final String EMAIL_PROMPT_MSG = "Email address...";
	private static final String GO_BACK_BTN = "Go back";
	private static final String TYPE_EMAIL_ADDR_TO_RECOVER_PWD_FOR = 
		"Password recovery starting phase:\ntype a valid email address below";

	private Scene scene;

	private Text emailAddressToRecPwdForText;
	private TextField emailAddressTextField;
	private Button sendRequestButton;
	private Button alreadyHaveTokenButton;
	private Button goBackBtn;

	private GraphicsController controller;

	public PasswordRecoveryView(ViewStack viewStack) {
		controller = new PasswordRecoveryViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		emailAddressToRecPwdForText = new Text(TYPE_EMAIL_ADDR_TO_RECOVER_PWD_FOR);
		emailAddressTextField = new TextField();
		sendRequestButton = new Button(BTN_MSG_SENDREQ);
		alreadyHaveTokenButton = new Button(BTN_MSG_GOTTOKEN);
		goBackBtn = new Button(GO_BACK_BTN);
		controller.setup();
	}

	private void setNodesProps() {
		emailAddressTextField.setPromptText(EMAIL_PROMPT_MSG);
		emailAddressToRecPwdForText.setFont(GraphicsUtil.getBoldFont());
	}

	private void populateScene() {
		VBox vbox = new VBox(10);
		vbox.getChildren().add(emailAddressToRecPwdForText);
		vbox.getChildren().add(emailAddressTextField);
		vbox.getChildren().add(sendRequestButton);
		vbox.getChildren().add(alreadyHaveTokenButton);
		vbox.getChildren().add(goBackBtn);

		scene = new Scene(vbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGHT);
	}
	
	@Override
	public Node[] getNodes() {
		return new Node[] {
			emailAddressTextField,
			sendRequestButton,
			alreadyHaveTokenButton,
			goBackBtn
		};
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

	public void setPresetEmail(String emailAddress) {
		emailAddressTextField.setText(emailAddress);
	}

	@Override
	public void visible() {
		// no need to update anything
	}

}
