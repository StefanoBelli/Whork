package logic.controller;

import java.util.Map;

import logic.bean.ChatInitBean;
import logic.bean.UserAuthBean;
import logic.controller.service.TokenizedServiceController;
import logic.dao.ChatLogDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.ChatLogEntryModel;
import logic.net.protocol.StatelessProtocol.Request;
import logic.net.protocol.StatelessProtocol.Response;
import logic.net.protocol.StatelessProtocol.Response.Status;
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
		chatInitBean.setToken(addToken(userAuthBean.getEmail()));
		chatInitBean.setServicePort(listenPort);

		return chatInitBean;
	}

	@RequestHandler("PushMessage")
	public Response pushMessage(Request request) {
		Map<String, String> headers = request.getHeaders();
		String senderEmail = getTokenAssocUserEmailFromHeaders(headers);
		
		if(senderEmail != null) {
			String receiverEmailField = headers.get("To");
			String contentLengthField = headers.get(CONTENT_LENGTH);
			String bodyField = request.getBody();

			if(receiverEmailField == null || contentLengthField == null || bodyField == null) {
				return buildMissingRequiredFieldResponse();
			}

			int contentLength = Integer.parseInt(contentLengthField);
			if(contentLength > 500) {
				return buildTooBigMessageResponse();
			}

			if(contentLength != bodyField.length()) {
				return buildIllegalArgumentResponse();
			}

			try {
				ChatLogEntryModel chatLogEntry = new ChatLogEntryModel();
				chatLogEntry.setText(bodyField);
				chatLogEntry.setSenderEmail(senderEmail);
				chatLogEntry.setReceiverEmail(receiverEmailField);
				
				ChatLogDao.addLogEntry(chatLogEntry);
				return buildOkAcceptedForDeliveryResponse();
			} catch(DataLogicException e) {
				return buildUserNotFoundResponse();
			} catch (DataAccessException e) {
				return buildGenericErrorResponse();
			}
		}

		return buildInvalidTokenResponse();
	}

	@RequestHandler("PullMessages")
	public Response pullMessages(Request request) {
		Map<String, String> headers = request.getHeaders();
		String senderEmail = getTokenAssocUserEmailFromHeaders(headers);
		
		if(senderEmail != null) {
			String contentLengthField = headers.get(CONTENT_LENGTH);
			String toField = headers.get("To");
			String tsFromLatest = headers.get("Ts-From-Latest");
			String tsToEarliest = headers.get("Ts-To-Earliest");

			if(contentLengthField != null && !contentLengthField.equals("0")) {
				return buildIllegalArgumentResponse();
			}

			if(toField == null || tsFromLatest == null || tsToEarliest == null) {
				return buildMissingRequiredFieldResponse();
			}

		}

		return buildInvalidTokenResponse();
	}

	@RequestHandler("CheckOnlineStatus")
	public Response checkOnlineStatus(Request request) {
		return null;
	}

	private Response buildTooBigMessageResponse() {
		Response tooBigMessageResponse = new Response();
		tooBigMessageResponse.addHeaderEntry(CONTENT_LENGTH, "13");
		tooBigMessageResponse.setBody("TooBigMessage");
		tooBigMessageResponse.setStatus(Status.KO);
		return tooBigMessageResponse;
	}

	private Response buildOkAcceptedForDeliveryResponse() {
		Response okResponse = new Response();
		okResponse.addHeaderEntry(CONTENT_LENGTH, "19");
		okResponse.setBody("AcceptedForDelivery");
		okResponse.setStatus(Status.OK);
		return okResponse;
	}
}
