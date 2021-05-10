package logic.net;

import java.nio.channels.SocketChannel;

public interface ReceiveEvent {
	void onReceive(SocketChannel socketChannel);
}
