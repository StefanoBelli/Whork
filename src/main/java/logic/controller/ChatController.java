package logic.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.bean.ChatInitBean;
import logic.model.ChatLogEntryModel;
import logic.net.Server;
import logic.net.WSServer;
import logic.net.protocol.StatelessProtocol;
import logic.net.protocol.StatelessProtocol.Request;
import logic.net.protocol.StatelessProtocol.Response;
import logic.net.protocol.StatelessProtocol.Response.Status;
import logic.net.protocol.annotation.RequestHandler;
import logic.util.Util;
import logic.util.tuple.Pair;
import logic.dao.ChatLogDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;

public final class ChatController {
	private ChatController() {}

	private static ChatController instance = null;

	public static ChatController getInstance() {
		if(instance == null) {
			instance = new ChatController();
		}

		return instance;
	}

	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String LISTEN_ADDR = Util.INADDR_ANY;
	private static final int VALID_TOKEN_INTVL_INTEGER = Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_SVC_INTVL_TOK); // secs
	private static final String VALID_TOKEN_INTVL_STRING = Integer.toString(VALID_TOKEN_INTVL_INTEGER);
	private static final String CURRENT_TOKEN_AND_USER_EMAIL_CANNOT_BOTH_NULL = "Either currentToken or userEmail is null, not both";
	private static final String CURRENT_TOKEN_AND_USER_EMAIL_CANNOT_BOTH_NOT_NULL = "Either currentToken or userEmail is NOT null, not both";
	private static final int SHOULD_PULL_MSGS_EVERY = 1000; //ms
	private static final String SHOULD_PULL_MSGS_EVERY_STRING = Integer.toString(SHOULD_PULL_MSGS_EVERY);
	private static final int VALID_TOKEN_INTVL = VALID_TOKEN_INTVL_INTEGER;
	private final StatelessProtocol statelessProtocol = new StatelessProtocol(this); //that's why we need a singleton
	private final Map<String, Pair<String, Long>> validTokens = new HashMap<>();
	private final int listenPort = Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_SVC_CHAT_PORT);
	private Server server;
	private boolean isOnline = false;

	public final boolean startService() {
		if (isOnline) {
			return false;
		}
		
		server = new Server(new WSServer(LISTEN_ADDR, listenPort,
				new Server.OnReceiveEventHandler(statelessProtocol)));

		server.start();

		isOnline = true;
		return true;
	}

	public final boolean stopService() {
		if (isOnline) {
			try {
				server.stop();
			} catch (IOException | InterruptedException e) {
				Util.exceptionLog(e);
				Thread.currentThread().interrupt();
			}

			isOnline = false;
			return true;
		}

		return false;
	}

	public final boolean isOnlineService() {
		return isOnline;
	}

	private final String addOrRefreshToken(String currentToken, String userEmail) {
		String token = null;

		if (currentToken == null) {
			if (userEmail != null) {
				removePreExistantTokenForUserEmail(userEmail);
				token = Util.generateToken();
				validTokens.put(token, new Pair<>(userEmail, Instant.now().getEpochSecond()));
			} else {
				throw new IllegalArgumentException(CURRENT_TOKEN_AND_USER_EMAIL_CANNOT_BOTH_NULL);
			}
		} else {
			if (userEmail == null) {
				Pair<String, Long> currentTokenRecord = validTokens.get(currentToken);
				if (currentTokenRecord != null) {
					token = Util.generateToken();
					validTokens.remove(currentToken);
					validTokens.put(token, new Pair<>(currentTokenRecord.getFirst(), Instant.now().getEpochSecond()));
				}
			} else {
				throw new IllegalArgumentException(CURRENT_TOKEN_AND_USER_EMAIL_CANNOT_BOTH_NOT_NULL);
			}
		}

		return token;
	}

	private void removePreExistantTokenForUserEmail(String userEmail) {
		for(final Map.Entry<String,Pair<String, Long>> tokenValues : validTokens.entrySet()) {
			if(tokenValues.getValue().getFirst().equals(userEmail)) {
				validTokens.remove(tokenValues.getKey());
				break;
			}
		}
	}

	private final String queryToken(String tok) {
		Pair<String, Long> tokenPair = validTokens.get(tok);

		if (tokenPair == null) {
			return null;
		}

		long t = Instant.now().getEpochSecond() - tokenPair.getSecond();
		if (t > VALID_TOKEN_INTVL_INTEGER) {
			validTokens.remove(tok);
			return null;
		}

		return tokenPair.getFirst();
	}

	private final String addToken(String userEmail) {
		return addOrRefreshToken(null, userEmail);
	}

	private final String getTokenAssocUserEmailFromHeaders(Map<String, String> headers) {
		String token = headers.get("Token");

		if (token != null) {
			return queryToken(token);
		}

		return null;
	}

	private final String isUserOnlineFmtResponse(String email) {
		for (final Pair<String, Long> v : validTokens.values()) {
			if (v.getFirst().equals(email)) {
				long t = Instant.now().getEpochSecond() - v.getSecond();
				if (t > VALID_TOKEN_INTVL_INTEGER) {
					return "0";
				}
				
				return "1";
			}
		}

		return "0";
	}

	public ChatInitBean newChatSession(String userEmail) {
		ChatInitBean chatInitBean = new ChatInitBean();
		chatInitBean.setShouldPullMessagesEvery(SHOULD_PULL_MSGS_EVERY);
		chatInitBean.setTokenExpiresIn(VALID_TOKEN_INTVL);
		chatInitBean.setToken(addToken(userEmail));
		chatInitBean.setServicePort(listenPort);

		return chatInitBean;
	}

	@RequestHandler("TokenRefresh")
	public final Response tokenRefresh(Request request) {
		Map<String, String> headers = request.getHeaders();

		String token = headers.get("Token");
		if (token != null && queryToken(token) != null) {
			String newToken = addOrRefreshToken(token, null);
			if (newToken == null) {
				return ResponseFactory.buildMissingRequiredFieldResponse();
			}

			return ResponseFactory.buildOkNewTokenResponse(newToken);
		}

		return ResponseFactory.buildInvalidTokenResponse();
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
				return ResponseFactory.buildTooBigMessageResponse();
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

			return ResponseFactory.buildOkOnlineStatusResponse(
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

		return ResponseFactory.buildOkSerializedMsgsResponse(jsonSerializedLogs);
	}

	private Response putChatLogEntryAndGetResponse(String bodyField, String senderEmail, 
			String toField) {
		ChatLogEntryModel chatLogEntry = new ChatLogEntryModel();
		chatLogEntry.setText(bodyField);
		chatLogEntry.setSenderEmail(senderEmail);
		chatLogEntry.setReceiverEmail(toField);
		chatLogEntry.setDeliveryRequestTime(new Date().getTime());

		try {	
			ChatLogDao.addLogEntry(chatLogEntry);
		} catch(DataLogicException e) {
			return ResponseFactory.buildUserNotFoundResponse();
		} catch(DataAccessException e) {
			Util.exceptionLog(e);
			return ResponseFactory.buildGenericErrorResponse();
		}

		return ResponseFactory.buildOkAcceptedForDeliveryResponse();
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

	private static final class ResponseFactory {
		private ResponseFactory() {}

		public static final Response buildGenericErrorResponse() {
			Response genericErrorResponse = new Response();
			genericErrorResponse.addHeaderEntry(CONTENT_LENGTH, "12");
			genericErrorResponse.setBody("GenericError");
			genericErrorResponse.setStatus(Status.KO);
			return genericErrorResponse;
		}

		public static final Response buildInvalidTokenResponse() {
			Response invalidTokenResponse = new Response();
			invalidTokenResponse.addHeaderEntry(CONTENT_LENGTH, "12");
			invalidTokenResponse.setBody("InvalidToken");
			invalidTokenResponse.setStatus(Status.KO);
			return invalidTokenResponse;
		}

		public static final Response buildOkNewTokenResponse(String newToken) {
			Response okResponse = new Response();
			okResponse.addHeaderEntry(CONTENT_LENGTH, Integer.toString(newToken.length()));
			okResponse.addHeaderEntry("Expires-In", VALID_TOKEN_INTVL_STRING);
			okResponse.setStatus(Status.OK);
			okResponse.setBody(newToken);
			return okResponse;
		}

		public static final Response buildMissingRequiredFieldResponse() {
			Response missingRequiredFieldsResponse = new Response();
			missingRequiredFieldsResponse.addHeaderEntry(CONTENT_LENGTH, "21");
			missingRequiredFieldsResponse.setBody("MissingRequiredFields");
			missingRequiredFieldsResponse.setStatus(Status.KO);
			return missingRequiredFieldsResponse;
		}

		public static final Response buildUserNotFoundResponse() {
			Response userNotFoundResponse = new Response();
			userNotFoundResponse.addHeaderEntry(CONTENT_LENGTH, "12");
			userNotFoundResponse.setBody("UserNotFound");
			userNotFoundResponse.setStatus(Status.KO);
			return userNotFoundResponse;
		}

		public static final Response buildIllegalArgumentResponse() {
			Response illegalArgumentResponse = new Response();
			illegalArgumentResponse.addHeaderEntry(CONTENT_LENGTH, "15");
			illegalArgumentResponse.setBody("IllegalArgument");
			illegalArgumentResponse.setStatus(Status.KO);
			return illegalArgumentResponse;
		}

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
