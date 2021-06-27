package logic.controller;

import logic.bean.ChatInitBean;
import logic.bean.UserAuthBean;
import logic.controller.service.TokenizedServiceController;
import logic.net.protocol.StatelessProtocol.Request;
import logic.net.protocol.StatelessProtocol.Response;
import logic.net.protocol.annotation.RequestHandler;
import logic.util.Util;

public final class ChatController extends TokenizedServiceController {
	protected ChatController() {
		super(Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_SVC_CHAT_PORT));
	}
	
	private static ChatController instance = null;
	private static final int SHOULD_PULL_MSGS_EVERY = 1000; //ms

	public static ChatController getInstance() {
		if(instance == null) {
			instance = new ChatController();
		}

		return instance;
	}
	
	public ChatInitBean newChatSession(UserAuthBean userAuthBean) {
		ChatInitBean chatInitBean = new ChatInitBean();
		chatInitBean.setShouldPullMessagesEvery(SHOULD_PULL_MSGS_EVERY);
		chatInitBean.setTokenExpiresIn(VALID_TOKEN_INTVL);
		chatInitBean.setToken(addOrRefreshToken(null, userAuthBean.getEmail()));

		return chatInitBean;
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
}
