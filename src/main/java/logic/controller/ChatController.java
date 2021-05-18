package logic.controller;

import logic.bean.ChatInitBean;
import logic.bean.UserBean;
import logic.controller.privileges.TokenAccessControl;
import logic.controller.service.ServiceController;
import logic.net.protocol.StatelessProtocol.OnRequestHandler;
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

	private static final class PushMessage implements OnRequestHandler {

		@RequestHandler("PushMessage")
		@Override
		public Response onRequest(Request request) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	private static final class PullMessages implements OnRequestHandler {

		@RequestHandler("PullMessages")
		@Override
		public Response onRequest(Request request) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static final class CheckOnlineStatus implements OnRequestHandler {

		@RequestHandler("CheckOnlineStatus")
		@Override
		public Response onRequest(Request request) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static final class TokenRefresh implements OnRequestHandler {

		@RequestHandler("TokenRefresh")
		@Override
		public Response onRequest(Request request) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@Override
	public boolean startService() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopService() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnlineService() {
		// TODO Auto-generated method stub
		return false;
	}

	public ChatInitBean newChatSession(UserBean user) {
		return null;
	}
}
