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
	private static final String FORGOT_PASSWORD_BTN_MSG = "Forgot password?";
	private static final String REGISTER_BTN_MSG = "I don't have an account";
	private static final String STAY_LOGGED_IN_MSG = "Stay logged in";
	private static final String LOGIN_BTN_MSG = "Login";
	private static final int CONFIG_WIN_WIDTH = 265;
	private static final int CONFIG_WIN_HEIGHT = 260;
	private static final String WIN_TITLE = LOGIN_TO_WHORK_MSG;

	private Scene scene;
	
	private Label emailMessage;
	private TextField emailField;
	private Label passwordMessage;
	private TextField passwordField;
	private Button registerButton;
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
		emailMessage = new Label(EMAIL_MSG);
		emailField = new TextField();
		passwordMessage = new Label(PASSWORD_MSG);
		passwordField = new PasswordField();
		forgotPasswordButton = new Button(FORGOT_PASSWORD_BTN_MSG);
		loginButton = new Button(LOGIN_BTN_MSG);
		stayLoggedInBox = new CheckBox(STAY_LOGGED_IN_MSG);
		registerButton = new Button(REGISTER_BTN_MSG);
		controller.setup();
	}

	private void setNodesProps() {
		emailField.setPromptText(EMAIL_PROMPT_MSG);
		passwordField.setPromptText(PASSWORD_PROMPT_MSG);
		stayLoggedInBox.setSelected(true);
		loginButton.setDisable(true);
	}

	private void populateScene() {
		VBox credVbox = new VBox(10);
		credVbox.getChildren().add(emailMessage);
		credVbox.getChildren().add(emailField);
		credVbox.getChildren().add(passwordMessage);
		credVbox.getChildren().add(passwordField);
		credVbox.getChildren().add(forgotPasswordButton);
		credVbox.getChildren().add(stayLoggedInBox);

		HBox logonHbox = new HBox(10);
		logonHbox.getChildren().add(loginButton);
		logonHbox.getChildren().add(registerButton);
		
		logonHbox.setPadding(new Insets(0,0,0,18));

		VBox rootVbox = new VBox(25);
		rootVbox.getChildren().add(credVbox);
		rootVbox.getChildren().add(logonHbox);

		scene = new Scene(rootVbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGHT);
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
			stayLoggedInBox,
			registerButton
		};
	}

	@Override
	public void visible() {
		//no need to update anything
	}
}
