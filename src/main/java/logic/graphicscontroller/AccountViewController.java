package logic.graphicscontroller;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.controller.OfferController;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.ViewStack;

public final class AccountViewController extends GraphicsController {
	public AccountViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		((Button)n[0]).setOnMouseClicked(new HandleLogoutRequest());
		
	}

	@Override
	public void update() {
		//no need to update anything
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
	
	public static final class HandleChatRequest implements EventHandler<MouseEvent>{
		private OfferBean offer;
		
		public HandleChatRequest(OfferBean offer){
			this.offer=offer;
		}

		@Override
		public void handle(MouseEvent event) {
			//TODO
		}
	}
	
	public static final class HandleCandidateRequest implements EventHandler<MouseEvent>{
		private OfferBean offer;
		
		public HandleCandidateRequest(OfferBean offer){
			this.offer=offer;
		}

		@Override
		public void handle(MouseEvent event) {
			try {
				OfferController.insertCandidature(offer.getId(), LoginHandler.getSessionUser().getCf());
			} catch (DataAccessException | DataLogicException e) {
				e.getSuppressed();
			}
		}
	}
	
}
