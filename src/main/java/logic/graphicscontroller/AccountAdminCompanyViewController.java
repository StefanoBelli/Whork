package logic.graphicscontroller;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import logic.bean.UserBean;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.PostOfferView;
import logic.view.ViewStack;

public class AccountAdminCompanyViewController extends GraphicsController {
	
	private static final double MAX_WIDTH = 200;
	private static final String WHORK = "whork";

	private Button homeBtn;
	private Button postOfferBtn;
	private Button logOutBtn;

	UserBean user;

	public AccountAdminCompanyViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setup() {
		Node[] n = view.getNodes();
		homeBtn = (Button) n[0];
		postOfferBtn = (Button) n[1];
		logOutBtn = (Button) n[2];

		user = LoginHandler.getSessionUser();

		setPostOfferButton();

		setListeners();
	}
	
	private void setListeners() {
		homeBtn.setOnMouseClicked(new HandleHomeRequest());
		postOfferBtn.setOnMouseClicked(new HandlePostOfferRequest());
		logOutBtn.setOnMouseClicked(new HandleLogoutRequest());
	}

	private void setPostOfferButton() {
		if(user.isRecruiter()) postOfferBtn.setVisible(true);
		else postOfferBtn.setVisible(false);
	}
	
	
	
	
	@Override
	public void update() {
		//no need to update anything
	}
	
	
	
	private final class HandleHomeRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			viewStack.pop();
		}
	}
	
	private final class HandlePostOfferRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			viewStack.push(new PostOfferView(viewStack));
		}
	}

	private final class HandleLogoutRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			LoginHandler.logout();
			
			try {
				Util.Files.overWriteJsonAuth(null, null);
			} catch(IOException e) {
				Util.exceptionLog(e);
				GraphicsUtil.closeStageByMouseEvent(event);
				GraphicsUtil.showExceptionStage(e);
			}

			viewStack.pop();
		}
	}
}



