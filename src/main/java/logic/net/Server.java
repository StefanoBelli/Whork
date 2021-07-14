package logic.net;


import java.io.IOException;

import org.java_websocket.WebSocket;

import logic.net.protocol.StatelessProtocol;
import logic.util.tuple.Pair;

public final class Server {
	private final WSServer webSocketServer;
	
	public Server(WSServer webSocketServer) {
		this.webSocketServer = webSocketServer;
	}

	public void start() {
		webSocketServer.start();
	}

	public void stop() 
			throws IOException, InterruptedException {
		webSocketServer.stop();
	}

	public static final class OnReceiveEventHandler implements ReceiveEvent {
		private final StatelessProtocol protocol;
		
		public OnReceiveEventHandler(StatelessProtocol protocol) {
			this.protocol = protocol;
		}

		@Override
		public void onReceive(WebSocket clientWs, String message) {
			Pair<String, Boolean> next = protocol.onMessage(message);
			String sendWhat = next.getFirst();
			boolean closeNow = Boolean.TRUE.equals(next.getSecond());

			if(sendWhat != null) {
				clientWs.send(next.getFirst());
			}

			if(closeNow) {
				clientWs.close();
			}
		}
		
	}

}
