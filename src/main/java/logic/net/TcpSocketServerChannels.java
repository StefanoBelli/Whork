package logic.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public final class TcpSocketServerChannels implements AutoCloseable {
	private Selector selector = Selector.open();
	private ServerSocketChannel serverSocket = ServerSocketChannel.open();
	private ReceiveEvent receiveEvent;
	
	public TcpSocketServerChannels(String listenAddress, int listenPort, ReceiveEvent receiveEvent) 
			throws IOException {
		serverSocket.bind(new InetSocketAddress(listenAddress, listenPort));
		serverSocket.configureBlocking(false);
		serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		this.receiveEvent = receiveEvent;
	}

	public final void process() 
			throws IOException {
		selector.select();
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		Iterator<SelectionKey> iter = selectedKeys.iterator();
		while (iter.hasNext()) {
			SelectionKey key = iter.next();

			if (key.isAcceptable()) {
				SocketChannel client = serverSocket.accept();
				client.configureBlocking(false);
				client.register(selector, SelectionKey.OP_READ);
			} else if (key.isReadable()) {
				receiveEvent.onReceive((SocketChannel) key.channel());
			}

			iter.remove();
		}
	}

	@Override
	public final void close() {
		try {
			for(final SelectionKey key : selector.keys()) {
				key.channel().close();
			}
			
			selector.close();
			serverSocket.close();
		} catch(IOException e) {
			//unhandled
		}
	}
}
