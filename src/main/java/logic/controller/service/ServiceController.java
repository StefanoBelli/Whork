package logic.controller.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import logic.net.Server;
import logic.net.TcpSocketServerChannels;
import logic.net.protocol.StatelessProtocol;
import logic.util.Util;
import logic.util.tuple.Pair;

public class ServiceController {
	protected ServiceController(int listenPort){
		this.listenPort = listenPort;
	}

	private static final String LISTEN_ADDR = Util.INADDR_ANY;
	protected static final int VALID_TOKEN_INTVL = 
		Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_SVC_INTVL_TOK); //secs

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
}
