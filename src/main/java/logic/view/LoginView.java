package logic.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.GraphicsController;
import logic.graphicscontroller.LoginViewController;

public final class LoginView implements ControllableView {

	private static final String LOGIN_TO_WHORK_MSG = "Login to Whork";
	private static final String EMAIL_MSG = "Email";
	private static final String EMAIL_PROMPT_MSG = "Type your email...";
	private static final String PASSWORD_MSG = "Password";
	private static final String PASSWORD_PROMPT_MSG = "Type your password...";
	private static final String FORGOT_PASSWORD_MSG = "Forgot your password?";
	private static final String FORGOT_PASSWORD_BTN_MSG = "Recover my password";
	private static final String STAY_LOGGED_IN_MSG = "Stay logged in";
	private static final String LOGIN_BTN_MSG = "Login";
	private static final int CONFIG_WIN_WIDTH = 265;
	private static final int CONFIG_WIN_HEIGHT = 300;
	private static final String WIN_TITLE = LOGIN_TO_WHORK_MSG;

	private Scene scene;
	
	private Label loginToWhorkMessage;
	private Label emailMessage;
	private TextField emailField;
	private Label passwordMessage;
	private TextField passwordField;
	private Label forgotPasswordMessage;
	private Button forgotPasswordButton;
	private Button loginButton;
	private CheckBox stayLoggedInBox;
	
	private GraphicsController controller;

	public LoginView(ViewStack viewStack) {
		controller = new LoginViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		loginToWhorkMessage = new Label(LOGIN_TO_WHORK_MSG);
		emailMessage = new Label(EMAIL_MSG);
		emailField = new TextField();
		passwordMessage = new Label(PASSWORD_MSG);
		passwordField = new PasswordField();
		forgotPasswordMessage = new Label(FORGOT_PASSWORD_MSG);
		forgotPasswordButton = new Button(FORGOT_PASSWORD_BTN_MSG);
		loginButton = new Button(LOGIN_BTN_MSG);
		stayLoggedInBox = new CheckBox(STAY_LOGGED_IN_MSG);
		controller.setup();
	}

	private void setNodesProps() {
		emailField.setPromptText(EMAIL_PROMPT_MSG);
		passwordField.setPromptText(PASSWORD_PROMPT_MSG);
		loginToWhorkMessage.setPadding(new Insets(10,10,10,10));
		stayLoggedInBox.setSelected(true);
	}

	private void populateScene() {
		VBox vbox = new VBox();
		vbox.getChildren().add(loginToWhorkMessage);
		vbox.getChildren().add(emailMessage);
		vbox.getChildren().add(emailField);
		vbox.getChildren().add(passwordMessage);
		vbox.getChildren().add(passwordField);

		VBox vboxfp = new VBox();
		vboxfp.getChildren().add(forgotPasswordMessage);
		vboxfp.getChildren().add(forgotPasswordButton);

		VBox vboxlg = new VBox();
		vboxlg.getChildren().add(loginButton);
		vboxlg.getChildren().add(stayLoggedInBox);

		HBox hbox = new HBox();
		hbox.getChildren().add(vboxlg);
		hbox.getChildren().add(vboxfp);

		vbox.getChildren().add(hbox);

		scene = new Scene(vbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGHT);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setResizable(false);
		stage.setTitle(WIN_TITLE);
	}

	@Override
	public Node[] getNodes() {
		return new Node[] {
			emailField,
			passwordField,
			loginButton,
			forgotPasswordButton,
			stayLoggedInBox
		};
	}

	@Override
	public void visible() {
		//no need to update anything
	}
}
