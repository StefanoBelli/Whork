package logic.graphicscontroller;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import logic.exception.InternalException;
import logic.exception.SyntaxException;
import logic.factory.BeanFactory;
import logic.factory.DialogFactory;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.ViewStack;

public final class LoginViewController extends GraphicsController {

	private static final String EMAIL_EXCEEDS_255_CHARS_MSG = "Email exceeds 255 chars!";
	private static final String EMAIL_EMPTY_MSG = "Email field is empty";
	private static final String PASSWORD_EMPTY_MSG = "Password field is empty";
	private static final String BOTH_EMPTY_MSG = "Both fields are empty";

	private TextField emailField;
	private TextField passwordField;
	private Label errorFieldLabel;
	private CheckBox stayLoggedInBox;

	public LoginViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		emailField = (TextField) n[0];
		passwordField = (TextField) n[1];
		((Button)n[2]).setOnMouseClicked(new HandleLoginRequest());
		((Button)n[3]).setOnMouseClicked(new HandlePasswordRecoveryRequest());
		errorFieldLabel = (Label) n[4];
		emailField.setOnMouseClicked(event -> errorFieldLabel.setVisible(false));
		passwordField.setOnMouseClicked(event -> errorFieldLabel.setVisible(false));
		stayLoggedInBox = (CheckBox) n[5];
	}
	
	private final class HandleLoginRequest implements EventHandler<MouseEvent> {

		private boolean checkEmailOrPasswordEmpty(String email, String password) {
			String msg = EMAIL_EMPTY_MSG;

			if(email.isEmpty()) { /* email empty && pwd ?? */
				if(password.isEmpty()) { /* email empty && pwd empty */
					msg = BOTH_EMPTY_MSG;
				} /* email empty && pwd not empty */
			} else { /* email not empty && email ?? */
				if(password.isEmpty()) { /* email not empty && password empty */
					msg = PASSWORD_EMPTY_MSG;
				} else { /* email not empty && password not empty */
					return false;
				}
			}

			errorFieldLabel.setText(msg);
			errorFieldLabel.setVisible(true);

			return true;
		}

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
			errorFieldLabel.setVisible(false);

			String email = emailField.getText();
			String password = passwordField.getText();

			if(checkEmailOrPasswordEmpty(email, password)) {
				return;
			}

			boolean outcome;

			try {
				outcome = LoginHandler.login(
							BeanFactory.buildUserAuthBean(email, password));
			} catch(InternalException e) {
				GraphicsUtil.closeStageByMouseEvent(event);
				GraphicsUtil.showExceptionStage(e);
				return;
			} catch(SyntaxException e) {
				errorFieldLabel.setText(EMAIL_EXCEEDS_255_CHARS_MSG);
				errorFieldLabel.setVisible(true);
				return;
			}

			completeLoginPhase(event, outcome, email, password);
		}

	}

	private final class HandlePasswordRecoveryRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			errorFieldLabel.setVisible(false);
			
			GraphicsUtil.closeStageByMouseEvent(event);
			GraphicsUtil.showExceptionStage(new Exception("missing implementation"));
			//TODO call appcontroller
		}

	}

	@Override
	public void update() {
		//no need to update anything
	}
	
}
