package logic.controller;

import logic.bean.ChatInitBean;
import logic.bean.UserBean;
import logic.controller.privileges.TokenAccessControl;
import logic.controller.service.ServiceController;
import logic.net.protocol.StatelessProtocol.Request;
import logic.net.protocol.StatelessProtocol.Response;
import logic.net.protocol.annotation.RequestHandler;

public final class ChatController extends TokenAccessControl implements ServiceController {
	private ChatController(){}

	private static ChatController instance = null;

	public static ChatController getInstance() {
		if(instance == null) {
			instance = new ChatController();
		}

		return instance;
	}

	@Override
	public boolean startService() {
		return false;
	}

	@Override
	public boolean stopService() {
		return false;
	}

	@Override
	public boolean isOnlineService() {
		return false;
	}

	public ChatInitBean newChatSession(UserBean user) {
		return null;
	}

	@RequestHandler("PushMessage")
	private Response pushMessage(Request request) {
		return null;
	}

	@RequestHandler("PullMessages")
	private Response pullMessages(Request request) {
		return null;
	}

	@RequestHandler("CheckOnlineStatus")
	private Response checkOnlineStatus(Request request) {
		return null;
	}

	@RequestHandler("TokenRefresh")
	private Response tokenRefresh(Request request) {
		return null;
	}
}
