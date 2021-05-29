package logic.controller.service;

import java.io.IOException;

import logic.controller.privileges.TokenAccessControl;
import logic.net.Server;
import logic.net.TcpSocketServerChannels;
import logic.net.protocol.StatelessProtocol;
import logic.util.Util;

public class ServiceController extends TokenAccessControl {
	protected ServiceController(int listenPort){
		this.listenPort = listenPort;
	}

	private static final String LISTEN_ADDR = Util.INADDR_ANY;

	private final StatelessProtocol statelessProtocol = new StatelessProtocol(this);
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

			return true;
		}

		return false;
	}

	public final boolean isOnlineService() {
		return isOnline;
	}
}
