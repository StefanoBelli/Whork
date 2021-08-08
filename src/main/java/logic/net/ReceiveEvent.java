package logic.net;

import org.java_websocket.WebSocket;

/**
 * @author Stefano Belli
 */
public interface ReceiveEvent {
	void onReceive(WebSocket clientWs, String message);
}
