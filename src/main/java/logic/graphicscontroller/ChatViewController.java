package logic.graphicscontroller;

import logic.controller.ChatController;
import logic.util.GraphicsUtil;
import logic.view.ControllableView;
import logic.view.ViewStack;

/**
 * future implementation
 */
public final class ChatViewController extends GraphicsController {

	public ChatViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		if(ChatController.getInstance().isOnlineService()) {
			GraphicsUtil.showExceptionStage(new IllegalArgumentException());
		}
	}

	@Override
	public void update() {
		//future implementation
	}
}
