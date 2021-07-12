package logic.graphicscontroller;

import javafx.scene.Node;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.view.ConfirmRegistrationView;
import logic.view.ControllableView;
import logic.view.RegisterJobSeekerView;
import logic.view.ViewStack;

public final class RegisterViewController extends GraphicsController {

	public RegisterViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		((Button)n[0]).setOnMouseClicked(new HandleJobSeekerRequest());
		((Button)n[1]).setOnMouseClicked(new HandleCompanyRequest());
		((Button)n[2]).setOnMouseClicked(new HandleConfirmRequest());
	}

	@Override
	public void update() {
		//no need to update anything
	}

	private static final class HandleJobSeekerRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			Stage stage = new Stage();
			ViewStack stack = new ViewStack(stage);
			stack.push(new RegisterJobSeekerView(stack));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		}
	}

	private static final class HandleCompanyRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub	
		}
	}

	private static final class HandleConfirmRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			Stage stage = new Stage();
			ViewStack stack = new ViewStack(stage);
			stack.push(new ConfirmRegistrationView(stack));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		}
	}
	
}
