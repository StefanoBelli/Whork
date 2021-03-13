package logic.graphicscontroller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import logic.controller.LoginController;
import logic.factory.DialogFactory;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.PasswordChangeView;
import logic.view.ViewStack;

public final class PasswordRecoveryViewController extends GraphicsController {

	private TextField emailAddressTextField;
	private Button continueButton;

	public PasswordRecoveryViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		emailAddressTextField = (TextField) n[0];
		continueButton = (Button) n[1];
		continueButton.setOnMouseClicked(new HandlePasswordRecoveryRequest());
		((Button)n[2]).setOnMouseClicked(new HandleAlreadyHasToken());
		emailAddressTextField.textProperty().addListener(new HandleChangedTextField());
	}

	@Override
	public void update() {
		//no need to update anything
	}

	private final class HandlePasswordRecoveryRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			String emailAddress = emailAddressTextField.getText();

			if(emailAddress.isBlank()) {
				DialogFactory.error(
					"Error", 
					"Empty field", 
					"Email address field is empty!").showAndWait();
			} else {
				LoginController.recoverPassword(emailAddress);

				StringBuilder builder = new StringBuilder();
				builder
					.append("If \"")
					.append(emailAddress)
					.append("\" is a valid and registered email address")
					.append(", you will recieve an email containing instructions (check spam)");

				DialogFactory.info(
					"Password change request", 
					"Check your inbox", 
					builder.toString()).showAndWait();
				
				emailAddressTextField.clear();
			}
		}

	}

	private final class HandleAlreadyHasToken implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			viewStack.push(new PasswordChangeView(viewStack));
		}

	}

	private final class HandleChangedTextField implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			continueButton.setDisable(
				newValue.isBlank() ||
				newValue.length() > 255 ||
				!Util.EMAIL_PATTERN.matcher(newValue).matches()
			);
		}
		
	}
}
