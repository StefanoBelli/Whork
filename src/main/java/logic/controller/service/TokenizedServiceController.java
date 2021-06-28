package logic.controller.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import logic.net.Server;
import logic.net.TcpSocketServerChannels;
import logic.net.protocol.StatelessProtocol;
import logic.net.protocol.StatelessProtocol.Request;
import logic.net.protocol.StatelessProtocol.Response;
import logic.net.protocol.StatelessProtocol.Response.Status;
import logic.net.protocol.annotation.RequestHandler;
import logic.util.Util;
import logic.util.tuple.Pair;

public class TokenizedServiceController {
	protected TokenizedServiceController(int listenPort){
		this.listenPort = listenPort;
	}

	protected static final String CONTENT_LENGTH = "Content-Length";
	private static final String LISTEN_ADDR = Util.INADDR_ANY;
	private static final int VALID_TOKEN_INTVL_INTEGER = 
		Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_SVC_INTVL_TOK); //secs
	private static final String VALID_TOKEN_INTVL_STRING = 
		Util.InstanceConfig.getString(Util.InstanceConfig.KEY_SVC_INTVL_TOK); //secs

	protected static final int VALID_TOKEN_INTVL = VALID_TOKEN_INTVL_INTEGER;
	private final StatelessProtocol statelessProtocol = new StatelessProtocol(this);
	private final Map<String, Pair<String, Long>> validTokens = new HashMap<>();
	protected final int listenPort;
	private Thread worker;
	private boolean isOnline = false;

	public final boolean startService() {
		if(isOnline) {
			return false;
		}

		try {
			worker = 
				new Thread(
					new Server(
						new TcpSocketServerChannels(LISTEN_ADDR, listenPort, 
							new Server.OnReceiveEventHandler(statelessProtocol))));
			
			worker.start();
		} catch (IOException | IllegalThreadStateException e) {
			Util.exceptionLog(e);
			return false;
		}

		isOnline = true;
		return true;
	}

	public final boolean stopService() {
		if(isOnline) {
			try {
				worker.interrupt();
				worker.join();
			} catch(SecurityException e) {
				Util.exceptionLog(e);
				return false;
			} catch(InterruptedException e) {
				Util.exceptionLog(e);
				isOnline = false;
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
		
		if(currentToken == null) {
			if(userEmail != null) {
				token = Util.generateToken();
				validTokens.put(token, new Pair<>(userEmail, new Date().getTime()));
			} else {
				throw new IllegalArgumentException("Either currentToken or userEmail is null, not both");
			}
		} else {
			if(userEmail == null) {
				Pair<String, Long> currentTokenRecord = validTokens.get(currentToken);
				if(currentTokenRecord != null) {
					token = Util.generateToken();
					validTokens.remove(currentToken);
					validTokens.put(token, new Pair<>(currentTokenRecord.getFirst(), new Date().getTime()));
				}
			} else {
				throw new IllegalArgumentException("Either currentToken or userEmail is NOT null, not both");
			}
		}
		
		return token;
	}

	private final String queryToken(String tok) {
		Date nowTime = new Date();
		Pair<String, Long> token = validTokens.get(tok);

		if(nowTime.getTime() - token.getSecond() > VALID_TOKEN_INTVL_INTEGER) {
			validTokens.remove(tok);
			return null;
		}

		return token.getFirst();
	}

	protected final String addToken(String userEmail) {
		return addOrRefreshToken(null, userEmail);
	}
	
	protected final String getTokenAssocUserEmailFromHeaders(Map<String, String> headers) {
		String token = headers.get("Token");

		if(token != null) {
			return queryToken(token);
		}

		return null;
	}

	@RequestHandler("TokenRefresh")
	public final Response tokenRefresh(Request request) {
		Map<String, String> headers = request.getHeaders();

		String token = headers.get("Token");
		if(token != null && queryToken(token) != null) {
			String newToken = addOrRefreshToken(token, null);
			if(newToken == null) {
				return buildMissingRequiredFieldResponse();
			}

			return buildOkNewTokenResponse(newToken);
		}

		return buildInvalidTokenResponse();
	}

	protected final Response buildGenericErrorResponse() {
		Response genericErrorResponse = new Response();
		genericErrorResponse.addHeaderEntry(CONTENT_LENGTH, "12");
		genericErrorResponse.setBody("GenericError");
		genericErrorResponse.setStatus(Status.KO);
		return genericErrorResponse;
	}

	protected final Response buildInvalidTokenResponse() {
		Response invalidTokenResponse = new Response();
		invalidTokenResponse.addHeaderEntry(CONTENT_LENGTH, "12");
		invalidTokenResponse.setBody("InvalidToken");
		invalidTokenResponse.setStatus(Status.KO);
		return invalidTokenResponse;
	}

	protected final Response buildOkNewTokenResponse(String newToken) {
		Response okResponse = new Response();
		okResponse.addHeaderEntry(CONTENT_LENGTH, Integer.toString(newToken.length()));
		okResponse.addHeaderEntry("Expires-In", VALID_TOKEN_INTVL_STRING);
		okResponse.setStatus(Status.OK);
		okResponse.setBody(newToken);
		return okResponse;
	}

	protected final Response buildMissingRequiredFieldResponse() {
		Response missingRequiredFieldsResponse = new Response();
		missingRequiredFieldsResponse.addHeaderEntry(CONTENT_LENGTH, "21");
		missingRequiredFieldsResponse.setBody("MissingRequiredFields");
		missingRequiredFieldsResponse.setStatus(Status.KO);
		return missingRequiredFieldsResponse;
	}

	protected final Response buildUserNotFoundResponse() {
		Response userNotFoundResponse = new Response();
		userNotFoundResponse.addHeaderEntry(CONTENT_LENGTH, "12");
		userNotFoundResponse.setBody("UserNotFound");
		userNotFoundResponse.setStatus(Status.KO);
		return userNotFoundResponse;
	}

	protected final Response buildIllegalArgumentResponse() {
		Response illegalArgumentResponse = new Response();
		illegalArgumentResponse.addHeaderEntry(CONTENT_LENGTH, "15");
		illegalArgumentResponse.setBody("IllegalArgument");
		illegalArgumentResponse.setStatus(Status.KO);
		return illegalArgumentResponse;
	}
}
