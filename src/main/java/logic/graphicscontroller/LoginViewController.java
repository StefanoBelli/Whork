package logic.graphicscontroller;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import logic.factory.BeanFactory;
import logic.factory.DialogFactory;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.PasswordRecoveryView;
import logic.view.RegisterView;
import logic.view.ViewStack;

public final class LoginViewController extends GraphicsController {

	private TextField emailField;
	private TextField passwordField;
	private CheckBox stayLoggedInBox;
	private Button loginButton;

	public LoginViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		emailField = (TextField) n[0];
		passwordField = (TextField) n[1];
		loginButton = (Button) n[2];
		loginButton.setOnMouseClicked(new HandleLoginRequest());
		((Button)n[3]).setOnMouseClicked(new HandlePasswordRecoveryRequest());
		stayLoggedInBox = (CheckBox) n[4];
		emailField.textProperty().addListener(new HandleChangedTextFields());
		passwordField.textProperty().addListener(new HandleChangedTextFields());
		((Button)n[5]).setOnMouseClicked(new HandleRegisterRequest());
	}
	
	@Override
	public void update() {
		//no need to update anything
	}

	private final class HandleLoginRequest implements EventHandler<MouseEvent> {

		private void completeLoginPhase(
			MouseEvent event, boolean loggedIn, String email, String password) {
			if(loggedIn) {
				if(stayLoggedInBox.isSelected()) {
					try {
						Util.Files.overWriteJsonAuth(email, password);
					} catch(IOException e) {
						GraphicsUtil.closeStageByMouseEvent(event);
						GraphicsUtil.showExceptionStage(e);
					}
				}

				GraphicsUtil.closeStageByMouseEvent(event);
			} else {
				DialogFactory.error(
					"Access denied", 
					"Unable to login", 
					"Wrong username and/or password").showAndWait();
			}
		}

		@Override
		public void handle(MouseEvent event) {
			String email = emailField.getText();
			String password = passwordField.getText();

			boolean outcome;

			try {
				outcome = LoginHandler.login(
							BeanFactory.buildUserAuthBean(email, password));
			} catch(Exception e) {
				GraphicsUtil.closeStageByMouseEvent(event);
				GraphicsUtil.showExceptionStage(e);
				return;
			}

			completeLoginPhase(event, outcome, email, password);
		}
	}

	private final class HandlePasswordRecoveryRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			String emailFieldText = emailField.getText();
			String email = emailFieldText.isBlank() ? null : emailFieldText;
			PasswordRecoveryView pwdRecView = new PasswordRecoveryView(viewStack);
			pwdRecView.setPresetEmail(email);
			viewStack.push(pwdRecView);
		}
	}

	private final class HandleChangedTextFields implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			String email = emailField.getText();

			loginButton.setDisable(
				email.isBlank() ||
				passwordField.getText().isBlank() ||
				email.length() > 255 ||
				!Util.EMAIL_PATTERN.matcher(email).matches());
		}
	}

	private final class HandleRegisterRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			viewStack.push(new RegisterView(viewStack));
		}
	}
}
