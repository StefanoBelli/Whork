package logic.graphicscontroller;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.view.AccountView;
import logic.view.ControllableView;
import logic.view.LoginView;
import logic.view.ViewStack;

public final class HomeViewController extends GraphicsController {

	private Button accountBtn;

	public HomeViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		accountBtn = (Button) n[0];
		accountBtn.setOnMouseClicked(new HandleAccountButtonRequest());

		dynamicViewUpdate();
	}

	private void dynamicViewUpdate() {
		if(LoginHandler.getSessionUser() != null) {
			accountBtn.setText("My account");
		} else {
			accountBtn.setText("Login");
		}
	}

	private final class HandleAccountButtonRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			if(LoginHandler.getSessionUser() == null) {
				Stage newStage = new Stage();
				ViewStack stack = new ViewStack(newStage);
				stack.push(new LoginView(stack));
				newStage.initModality(Modality.APPLICATION_MODAL);
				newStage.showAndWait();
				
				dynamicViewUpdate();
			} else {
				viewStack.push(new AccountView(viewStack));
			}
		}
	}

	@Override
	public void update() {
		dynamicViewUpdate();
	}
	
}
