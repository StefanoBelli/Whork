package logic.graphicscontroller;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

import javax.swing.event.ChangeEvent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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
	}

	@Override
	public void update() {
		//no need to update anything
	}

	private static final class HandleCompleteRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private static final class HandleGoBackRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private static final class HandleChangedTextFields implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue observable, String oldValue, String newValue) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
