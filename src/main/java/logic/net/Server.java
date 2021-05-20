package logic.net;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

import logic.net.protocol.StatelessProtocol;
import logic.util.Util;
import logic.util.tuple.Pair;

public final class Server implements Runnable {
	private final TcpSocketServerChannels serverChannels;
	private final StatelessProtocol protocol;
	
	//may support SSL "SslSocketServerChannels" in the future... maybe
	public Server(TcpSocketServerChannels serverChannels, StatelessProtocol protocol) {
		this.serverChannels = serverChannels;
		this.protocol = protocol;
	}

	@Override
	public void run() {
		Thread curThread = Thread.currentThread();
		while(!curThread.isInterrupted()){
			try{
				serverChannels.process();
			} catch (IOException e) {
				Util.exceptionLog(e);
				break;
			}
		}

		serverChannels.close();
	}

	public final class OnReceiveEventHandler implements ReceiveEvent {

		private void socketChannelCloseWithLogging(SocketChannel socketChannel) {
			try {
				socketChannel.close();
			} catch(IOException e) {
				Util.exceptionLog(e);
			}
		}

		@Override
		public void onReceive(SocketChannel socketChannel) {
			String recvWhat;
			try {
				recvWhat = Util.SocketChannels.read(socketChannel);
			} catch(EOFException | SocketException e) {
				socketChannelCloseWithLogging(socketChannel);
				return;
			} catch(IOException e) {
				Util.exceptionLog(e);
				socketChannelCloseWithLogging(socketChannel);
				return;
			}

			Pair<String, Boolean> next = protocol.onMessage(recvWhat);
			String sendWhat = next.getFirst();
			boolean closeNow = Boolean.TRUE.equals(next.getSecond());

			if(sendWhat != null) {
				try {
					Util.SocketChannels.write(socketChannel, next.getFirst());
				} catch (IOException e) {
					Util.exceptionLog(e);
					socketChannelCloseWithLogging(socketChannel);
					return;
				}
			}

			if(closeNow) {
				socketChannelCloseWithLogging(socketChannel);
			}
		}
		
	}

}
