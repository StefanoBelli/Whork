package logic.net;

import org.java_websocket.WebSocket;

public interface ReceiveEvent {
	void onReceive(WebSocket clientWs, String message);
}
