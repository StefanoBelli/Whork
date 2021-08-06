package logic.graphicscontroller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import logic.view.ControllableView;
import logic.view.ViewStack;
import logic.controller.RegisterController;
import logic.exception.InternalException;
import logic.factory.DialogFactory;
import logic.util.Util;

public final class ConfirmRegistrationViewController extends GraphicsController {
	private TextField tokenField;
	private TextField emailField;
	private Button okButton;

	public ConfirmRegistrationViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		tokenField = (TextField) n[0];
		emailField = (TextField) n[1];
		okButton = (Button) n[2];
		tokenField.textProperty().addListener(new HandleChangedTextFields());
		emailField.textProperty().addListener(new HandleChangedTextFields());
		okButton.setOnMouseClicked(new HandleOkButton());
	}

	@Override
	public void update() {
		//no need to update anything
	}

	private final class HandleChangedTextFields implements ChangeListener<String> {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			okButton.setDisable(
				tokenField.getText().isBlank() ||
				emailField.getText().isBlank() ||
				emailField.getText().length() > 255 ||
				!Util.EMAIL_PATTERN.matcher(emailField.getText()).matches()
			);
		}
	}

	private final class HandleOkButton implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			try {
				RegisterController.confirm(emailField.getText(), tokenField.getText());
				DialogFactory.info(
					"Success - Whork",
					"Your account is confirmed!",
					"You may now login with your account").showAndWait();
				((Stage) view.getScene().getWindow()).close();
			} catch (InternalException e) {
				DialogFactory.error(
					"Error - Whork", 
					"We were unable to confirm your account!", 
					e.getMessage()).showAndWait();
			}
		}
		
	}
}
