package logic.controller;

import logic.bean.ChatInitBean;
import logic.bean.UserBean;
import logic.controller.service.ServiceController;
import logic.net.protocol.StatelessProtocol.Request;
import logic.net.protocol.StatelessProtocol.Response;
import logic.net.protocol.annotation.RequestHandler;
import logic.util.Util;

public final class ChatController extends ServiceController {
	private ChatController() {
		super(Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_CHATSERV_PORT));
	}
	
	private static ChatController instance = null;
	
	public static ChatController getInstance() {
		if(instance == null) {
			instance = new ChatController();
		}

		return instance;
	}

	public ChatInitBean newChatSession(UserBean user) {
		return null;
	}

	@RequestHandler("PushMessage")
	public Response pushMessage(Request request) {
		return null;
	}

	@RequestHandler("PullMessages")
	public Response pullMessages(Request request) {
		return null;
	}

	@RequestHandler("CheckOnlineStatus")
	public Response checkOnlineStatus(Request request) {
		return null;
	}

	@RequestHandler("TokenRefresh")
	public Response tokenRefresh(Request request) {
		return null;
	}
}
