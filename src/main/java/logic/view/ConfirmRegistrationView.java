package logic.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.ConfirmRegistrationViewController;
import logic.graphicscontroller.GraphicsController;

public final class ConfirmRegistrationView implements ControllableView {
	private Scene scene;

	private static final String TOKEN_MSG = "Token:";
	private static final String TOKEN_FIELD_HINT = "Type your token here...";
	private static final String EMAIL_MSG = "Email address:";
	private static final String EMAIL_FIELD_HINT = "Type your email address here...";
	private static final String GO_BACK_BTN = "Go back";
	private static final String OK_BTN = "OK";	
	private static final int CONFIG_WIN_WIDTH = 265;
	private static final int CONFIG_WIN_HEIGHT = 260;
	private static final String WIN_TITLE = "Confirm registration - Whork";

	private Label tokenMessage;
	private TextField tokenField;
	private Label emailMessage;
	private TextField emailField;
	private Button okButton;
	private Button goBackBtn;

	private GraphicsController controller;

	public ConfirmRegistrationView(ViewStack viewStack) {
		controller = new ConfirmRegistrationViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}
	
	private void populateScene() {
		VBox vbox = new VBox(10);
		vbox.getChildren().add(tokenMessage);
		vbox.getChildren().add(tokenField);
		vbox.getChildren().add(emailMessage);
		vbox.getChildren().add(emailField);
		vbox.getChildren().add(okButton);
		vbox.getChildren().add(goBackBtn);

		scene = new Scene(vbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGHT);
	}

	private void setNodesProps() {
		tokenField.setPromptText(TOKEN_FIELD_HINT);
		emailField.setPromptText(EMAIL_FIELD_HINT);
		okButton.setDisable(true);
	}

	private void init() {
		tokenMessage = new Label(TOKEN_MSG);
		tokenField = new TextField();
		emailMessage = new Label(EMAIL_MSG);
		emailField = new TextField();
		okButton = new Button(OK_BTN);
		goBackBtn = new Button(GO_BACK_BTN);
		controller.setup();
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
	public void visible() {
		//no need to update anything
	}

	@Override
	public Node[] getNodes() {
		return new Node[] { 
			tokenField,
			emailField,
			okButton,
			goBackBtn
		};
	}
}
