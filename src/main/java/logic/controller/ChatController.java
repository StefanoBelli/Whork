package logic.controller;

import java.util.Map;

import logic.bean.ChatInitBean;
import logic.bean.UserAuthBean;
import logic.controller.service.ServiceController;
import logic.net.protocol.StatelessProtocol.Request;
import logic.net.protocol.StatelessProtocol.Response;
import logic.net.protocol.StatelessProtocol.Response.Status;
import logic.net.protocol.annotation.RequestHandler;
import logic.util.Util;

public final class ChatController extends ServiceController {
	private ChatController() {
		super(Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_SVC_CHAT_PORT));

		invalidTokenResponse = new Response();
		invalidTokenResponse.addHeaderEntry(CONTENT_LENGTH, "12");
		invalidTokenResponse.setBody("InvalidToken");
		invalidTokenResponse.setStatus(Status.KO);

		genericErrorResponse = new Response();
		genericErrorResponse.addHeaderEntry(CONTENT_LENGTH, "12");
		genericErrorResponse.setBody("GenericError");
		genericErrorResponse.setStatus(Status.KO);
	}
	
	private static ChatController instance = null;
	private static final int SHOULD_PULL_MSGS_EVERY = 1000; //ms
	private static final String CONTENT_LENGTH = "Content-Length";
	private final Response invalidTokenResponse;
	private final Response genericErrorResponse;
	
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

	@RequestHandler("TokenRefresh")
	public Response tokenRefresh(Request request) {
		Map<String, String> headers = request.getHeaders();
		if(isValidToken(headers)) {
			String newToken = addOrRefreshToken(headers.get("Token"), null);
			if(newToken == null) {
				return genericErrorResponse;
			}

			Response okResponse = new Response();
			okResponse.addHeaderEntry(CONTENT_LENGTH, Integer.toString(newToken.length()));
			okResponse.addHeaderEntry("Expires-In", Integer.toString(VALID_TOKEN_INTVL));
			okResponse.setStatus(Status.OK);
			okResponse.setBody(newToken);
			
			return okResponse;
		}

		return invalidTokenResponse;
	}

	private boolean isValidToken(Map<String, String> headers) {
		String token = headers.get("Token");
		return token != null && queryToken(token) != null;
	}
}
