package logic.net;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * We may suppport a SSL/TLS version in the future...
 */
public final class WSServer extends WebSocketServer {
	private final ReceiveEvent receiveEvent;

	public WSServer(String listenAddr, int port, ReceiveEvent receiveEvent) {
		super(new InetSocketAddress(listenAddr, port));
		this.receiveEvent = receiveEvent;
	}

	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		//unused
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		//unused
	}

	@Override
	public void onMessage(WebSocket clientWs, String message) {
		receiveEvent.onReceive(clientWs, message);
	}

	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		//unused
	}

	@Override
	public void onStart() {
		//unused
	}
}
