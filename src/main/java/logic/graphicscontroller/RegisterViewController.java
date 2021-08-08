package logic.graphicscontroller;

import javafx.scene.Node;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import logic.util.GraphicsUtil;
import logic.view.ConfirmRegistrationView;
import logic.view.ControllableView;
import logic.view.RegisterCompanyView;
import logic.view.RegisterJobSeekerView;
import logic.view.ViewStack;

/**
 * @author Stefano Belli
 */
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
		((Button)n[3]).setOnMouseClicked(new GraphicsUtil.HandleGoBackRequest(viewStack));
	}

	@Override
	public void update() {
		//no need to update anything
	}

	private static final class HandleJobSeekerRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			GraphicsUtil.showAndWaitWindow(RegisterJobSeekerView.class);
		}
	}

	private static final class HandleCompanyRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			GraphicsUtil.showAndWaitWindow(RegisterCompanyView.class);
		}
	}

	private final class HandleConfirmRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			viewStack.push(new ConfirmRegistrationView(viewStack));
		}
	}
}
