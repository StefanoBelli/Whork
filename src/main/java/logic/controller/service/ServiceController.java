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
	private static final int VALID_TOKEN_INTVL = Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_SVC_INTVL_TOK); //seconds

	private final StatelessProtocol statelessProtocol = new StatelessProtocol(this);
	private final Map<String, Pair<String, Long>> validTokens = new HashMap<>();
	private final int listenPort;
	private Thread worker;
	private boolean isOnline = false;

	public final boolean startService() {
		if(isOnlineService()) {
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
		if(isOnlineService()) {
			try {
				worker.interrupt();
			} catch(SecurityException e) {
				Util.exceptionLog(e);
				return false;
			}

			isOnline = false;
			return true;
		}

		return false;
	}

	public final boolean isOnlineService() {
		return isOnline;
	}

	protected final void addOrRefreshToken(String userEmail) {
		validTokens.put(Util.generateToken(), new Pair<>(userEmail, new Date().getTime()));
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
