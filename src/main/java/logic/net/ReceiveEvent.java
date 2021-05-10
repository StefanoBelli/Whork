package logic.net;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface ReceiveEvent {
	void onReceive(SocketChannel socketChannel, ByteBuffer buffer);
}
