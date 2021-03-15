package logic.graphicscontroller;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import logic.controller.LoginController;
import logic.factory.DialogFactory;
import logic.util.GraphicsUtil;
import logic.view.ControllableView;
import logic.view.ViewStack;

public final class PasswordChangeViewController extends GraphicsController {

	private TextField tokenTextField;
	private TextField passwordTextField;
	private TextField retypePasswordTextField;
	private Button completeRequestButton;

	public PasswordChangeViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		tokenTextField = (TextField) n[0];
		passwordTextField = (TextField) n[1];
		retypePasswordTextField = (TextField) n[2];
		completeRequestButton = (Button) n[3];
		completeRequestButton.setOnMouseClicked(new HandleCompleteRequest());
		((Button)n[4]).setOnMouseClicked(new HandleGoBackRequest());
		passwordTextField.textProperty().addListener(new HandleChangedTextFields());
		tokenTextField.textProperty().addListener(new HandleChangedTextFields());
		retypePasswordTextField.textProperty().addListener(new HandleChangedTextFields());
	}

	@Override
	public void update() {
		//no need to update anything
	}

	private final class HandleCompleteRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			String token = tokenTextField.getText();
			String password = passwordTextField.getText();

			if(LoginController.changePassword(token, password)) {
				DialogFactory.info(
					"Whork - Password changed", 
					"Password change request", 
					"Your password was changed correctly!").showAndWait();

				GraphicsUtil.closeStageByMouseEvent(event);
			} else {
				DialogFactory.error(
					"Whork - Error",
					"Unable to change your password",
					"Something went wrong. Is your token correct?").showAndWait();
			}
		}
		
	}

	private final class HandleGoBackRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			viewStack.pop();
		}
		
	}

	private final class HandleChangedTextFields implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			String password = passwordTextField.getText();
			String retypePassword = retypePasswordTextField.getText();

			completeRequestButton.setDisable(
				tokenTextField.getText().isBlank() ||
				password.isBlank() ||
				retypePassword.isBlank() ||
				!password.equals(retypePassword)
			);
		}
	}
}
