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

		invalidTokenResponse = new Response();
		invalidTokenResponse.addHeaderEntry(CONTENT_LENGTH, "12");
		invalidTokenResponse.setBody("InvalidToken");
		invalidTokenResponse.setStatus(Status.KO);

		genericErrorResponse = new Response();
		genericErrorResponse.addHeaderEntry(CONTENT_LENGTH, "12");
		genericErrorResponse.setBody("GenericError");
		genericErrorResponse.setStatus(Status.KO);
	}

	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String LISTEN_ADDR = Util.INADDR_ANY;
	protected static final int VALID_TOKEN_INTVL = 
		Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_SVC_INTVL_TOK); //secs

	protected final Response invalidTokenResponse;
	protected final Response genericErrorResponse;
	private final StatelessProtocol statelessProtocol = new StatelessProtocol(this);
	private final Map<String, Pair<String, Long>> validTokens = new HashMap<>();
	private final int listenPort;
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

	protected final String addOrRefreshToken(String currentToken, String userEmail) {
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

	protected final String queryToken(String tok) {
		Date nowTime = new Date();
		Pair<String, Long> token = validTokens.get(tok);

		if(nowTime.getTime() - token.getSecond() > VALID_TOKEN_INTVL) {
			validTokens.remove(tok);
			return null;
		}

		return token.getFirst();
	}
	
	private final String getTokenAssocUserEmail(String token) {
		if(token != null) {
			return queryToken(token);
		}

		return null;
	}

	@RequestHandler("TokenRefresh")
	public final Response tokenRefresh(Request request) {
		Map<String, String> headers = request.getHeaders();

		String token = headers.get("Token");
		String userEmail = getTokenAssocUserEmail(token);

		if(userEmail != null) {
			String newToken = addOrRefreshToken(token, null);
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
}
