package logic.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.bean.ChatInitBean;
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
	private static final String SHOULD_PULL_MSGS_EVERY_STRING = 
		Integer.toString(SHOULD_PULL_MSGS_EVERY);

	public static ChatController getInstance() {
		if(instance == null) {
			instance = new ChatController();
		}

		return instance;
	}
	
	public ChatInitBean newChatSession(String userEmail) {
		ChatInitBean chatInitBean = new ChatInitBean();
		chatInitBean.setShouldPullMessagesEvery(SHOULD_PULL_MSGS_EVERY);
		chatInitBean.setTokenExpiresIn(VALID_TOKEN_INTVL);
		chatInitBean.setToken(addToken(userEmail));
		chatInitBean.setServicePort(listenPort);

		return chatInitBean;
	}

	@RequestHandler("PushMessage")
	public Response pushMessage(Request request) {
		Map<String, String> headers = request.getHeaders();
		String senderEmail = getTokenAssocUserEmailFromHeaders(headers);
		
		if(senderEmail != null) {
			String toField = headers.get("To");
			String contentLengthField = headers.get(CONTENT_LENGTH);
			String bodyField = request.getBody();

			if(toField == null || contentLengthField == null || bodyField == null) {
				return ResponseFactory.buildMissingRequiredFieldResponse();
			}

			if (senderEmail.equalsIgnoreCase(toField)) {
				return ResponseFactory.buildIllegalArgumentResponse();
			}

			int contentLength;
			
			try {
				contentLength = Integer.parseInt(contentLengthField);
			} catch (NumberFormatException e) {
				return ResponseFactory.buildIllegalArgumentResponse();
			}

			if(contentLength > 500) {
				return ChatResponseFactory.buildTooBigMessageResponse();
			}

			if(contentLength != bodyField.length()) {
				return ResponseFactory.buildIllegalArgumentResponse();
			}

			return putChatLogEntryAndGetResponse(bodyField, senderEmail, toField);
		}

		return ResponseFactory.buildInvalidTokenResponse();
	}

	@RequestHandler("PullMessages")
	public Response pullMessages(Request request) {
		Map<String, String> headers = request.getHeaders();
		String senderEmail = getTokenAssocUserEmailFromHeaders(headers);
		
		if(senderEmail != null) {
			String contentLengthField = headers.get(CONTENT_LENGTH);
			String toField = headers.get("To");
			String tsFromLatestField = headers.get("Ts-From-Latest");
			String tsToEarliestField = headers.get("Ts-To-Earliest");

			if(contentLengthField != null && !contentLengthField.equals("0")) {
				return ResponseFactory.buildIllegalArgumentResponse();
			}

			if(toField == null || tsFromLatestField == null || tsToEarliestField == null) {
				return ResponseFactory.buildMissingRequiredFieldResponse();
			}

			if (senderEmail.equalsIgnoreCase(toField)) {
				return ResponseFactory.buildIllegalArgumentResponse();
			}

			int tsFromLatest;
			int tsToEarliest;
			
			try {
				tsFromLatest = Integer.parseInt(tsFromLatestField);
				tsToEarliest = Integer.parseInt(tsToEarliestField);
			} catch(NumberFormatException e) {
				return ResponseFactory.buildIllegalArgumentResponse();
			}

			if(tsFromLatest < tsToEarliest) {
				return ResponseFactory.buildIllegalArgumentResponse();
			}

			return retrieveLogAndGetJsonSerResponse(
				senderEmail, toField, tsToEarliest, tsFromLatest);
		}

		return ResponseFactory.buildInvalidTokenResponse();
	}

	@RequestHandler("CheckOnlineStatus")
	public Response checkOnlineStatus(Request request) {
		Map<String, String> headers = request.getHeaders();
		String senderEmail = getTokenAssocUserEmailFromHeaders(headers);
		
		if(senderEmail != null) {
			String contentLengthField = headers.get(CONTENT_LENGTH);
			String toField = headers.get("To");

			if(toField == null) {
				return ResponseFactory.buildMissingRequiredFieldResponse();
			}

			if (senderEmail.equalsIgnoreCase(toField)) {
				return ResponseFactory.buildIllegalArgumentResponse();
			}

			if (contentLengthField != null && !contentLengthField.equals("0")) {
				return ResponseFactory.buildIllegalArgumentResponse();
			}

			return ChatResponseFactory.buildOkOnlineStatusResponse(
				isUserOnlineFmtResponse(toField)
			);
		}

		return ResponseFactory.buildInvalidTokenResponse();
	}

	private Response retrieveLogAndGetJsonSerResponse(String senderEmail, String toField, 
			int tsToEarliest, int tsFromLatest) {
		List<ChatLogEntryModel> logs;

		try {
			logs = 
				ChatLogDao.getLog(senderEmail, toField, tsToEarliest, tsFromLatest);
		} catch(DataAccessException e) {
			Util.exceptionLog(e);
			return ResponseFactory.buildGenericErrorResponse();
		}

		String jsonSerializedLogs = jsonSerializeChatLogEntries(logs);
		if(jsonSerializedLogs == null) {
			return ResponseFactory.buildGenericErrorResponse();
		}

		return ChatResponseFactory.buildOkSerializedMsgsResponse(jsonSerializedLogs);
	}

	private Response putChatLogEntryAndGetResponse(String bodyField, String senderEmail, 
			String toField) {
		ChatLogEntryModel chatLogEntry = new ChatLogEntryModel();
		chatLogEntry.setText(bodyField);
		chatLogEntry.setSenderEmail(senderEmail);
		chatLogEntry.setReceiverEmail(toField);

		try {	
			ChatLogDao.addLogEntry(chatLogEntry);
		} catch(DataLogicException e) {
			return ResponseFactory.buildUserNotFoundResponse();
		} catch(DataAccessException e) {
			Util.exceptionLog(e);
			return ResponseFactory.buildGenericErrorResponse();
		}

		return ChatResponseFactory.buildOkAcceptedForDeliveryResponse();
	}


	private String jsonSerializeChatLogEntries(List<ChatLogEntryModel> logs) {
		JSONArray jsonArray = new JSONArray();

		for (final ChatLogEntryModel chatLogEntry : logs) {
			JSONObject chatLogEntryJsonObject = new JSONObject();
			chatLogEntryJsonObject.put("id", chatLogEntry.getLogEntryId());
			chatLogEntryJsonObject.put("delivery_request_date", chatLogEntry.getDeliveryRequestTime());
			chatLogEntryJsonObject.put("sender_email", chatLogEntry.getSenderEmail());
			chatLogEntryJsonObject.put("receiver_email", chatLogEntry.getReceiverEmail());
			chatLogEntryJsonObject.put("text", chatLogEntry.getText());

			jsonArray.put(chatLogEntryJsonObject);
		}

		return jsonArray.toString();
	}

	private static final class ChatResponseFactory {
		private ChatResponseFactory() {}

		public static Response buildTooBigMessageResponse() {
			Response tooBigMessageResponse = new Response();
			tooBigMessageResponse.addHeaderEntry(CONTENT_LENGTH, "13");
			tooBigMessageResponse.setBody("TooBigMessage");
			tooBigMessageResponse.setStatus(Status.KO);
			return tooBigMessageResponse;
		}

		public static Response buildOkAcceptedForDeliveryResponse() {
			Response okResponse = new Response();
			okResponse.addHeaderEntry(CONTENT_LENGTH, "19");
			okResponse.setBody("AcceptedForDelivery");
			okResponse.setStatus(Status.OK);
			return okResponse;
		}

		public static Response buildOkSerializedMsgsResponse(String jsonSerializedLogs) {
			Response okResponse = new Response();
			okResponse.addHeaderEntry("Content-Type", "text/json");
			okResponse.addHeaderEntry(CONTENT_LENGTH, Integer.toString(jsonSerializedLogs.length()));
			okResponse.addHeaderEntry("Should-Pull-Every", SHOULD_PULL_MSGS_EVERY_STRING);
			okResponse.setBody(jsonSerializedLogs);
			okResponse.setStatus(Status.OK);
			return okResponse;
		}
	
		public static Response buildOkOnlineStatusResponse(String userOnlineFmtResponse) {
			Response okResponse = new Response();
			okResponse.addHeaderEntry(CONTENT_LENGTH, "1");
			okResponse.addHeaderEntry("Should-Pull-Every", SHOULD_PULL_MSGS_EVERY_STRING);
			okResponse.setBody(userOnlineFmtResponse);
			okResponse.setStatus(Status.OK);
			return okResponse;
		}
	}
}
