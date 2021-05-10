package logic.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public final class SSLServer extends SSLPeer {
	private boolean active;
    private SSLContext context;
    private Selector selector;

    public SSLServer(String protocol, String hostAddress, int port, String jksKeyManagersPath, String jksTrustManagersPath) 
			throws NoSuchAlgorithmException, IOException, 
				KeyManagementException, UnrecoverableKeyException, 
				KeyStoreException, CertificateException {

        context = SSLContext.getInstance(protocol);
        context.init(
			createKeyManagers(jksKeyManagersPath, "storepass", "keypass"), 
			createTrustManagers(jksTrustManagersPath, "storepass"), 
			new SecureRandom());

        SSLSession dummySession = context.createSSLEngine().getSession();
        myAppData = ByteBuffer.allocate(dummySession.getApplicationBufferSize());
        myNetData = ByteBuffer.allocate(dummySession.getPacketBufferSize());
        peerAppData = ByteBuffer.allocate(dummySession.getApplicationBufferSize());
        peerNetData = ByteBuffer.allocate(dummySession.getPacketBufferSize());
        dummySession.invalidate();

        selector = SelectorProvider.provider().openSelector();
        try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
        	serverSocketChannel.configureBlocking(false);
        	serverSocketChannel.socket().bind(new InetSocketAddress(hostAddress, port));
        	serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		}
        
        active = true;
    }

    public void start() throws IOException {
        while (active) {
            selector.select();
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = selectedKeys.next();
				
                selectedKeys.remove();
                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read((SocketChannel) key.channel(), (SSLEngine) key.attachment());
                }
            }
        }
    }

    public void stop() {
    	active = false;
    	executor.shutdown();
    	selector.wakeup();
    }

    private void accept(SelectionKey key) 
			throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);

        SSLEngine engine = context.createSSLEngine();
        engine.setUseClientMode(false);
        engine.beginHandshake();

        if (doHandshake(socketChannel, engine)) {
            socketChannel.register(selector, SelectionKey.OP_READ, engine);
        } else {
            socketChannel.close();
        }
    }

    @Override
    protected void read(SocketChannel socketChannel, SSLEngine engine) 
			throws IOException {
        peerNetData.clear();
        int bytesRead = socketChannel.read(peerNetData);
        if (bytesRead > 0) {
            peerNetData.flip();
            while (peerNetData.hasRemaining()) {
                peerAppData.clear();
                SSLEngineResult result = engine.unwrap(peerNetData, peerAppData);
                switch (result.getStatus()) {
                case OK:
                    peerAppData.flip();
                    //log.debug("Incoming message: " + new String(peerAppData.array()));
                    break;
                case BUFFER_OVERFLOW:
                    peerAppData = enlargeApplicationBuffer(engine, peerAppData);
                    break;
                case BUFFER_UNDERFLOW:
                    peerNetData = handleBufferUnderflow(engine, peerNetData);
                    break;
                case CLOSED:
                    closeConnection(socketChannel, engine);
                    return;
                default:
                    throw new IllegalStateException(getSSLInvalidStatusMessage(result));
                }
            }

        } else if (bytesRead < 0) {
            handleEndOfStream(socketChannel, engine);
        }
    }

    @Override
    protected void write(SocketChannel socketChannel, SSLEngine engine, String message) 
			throws IOException {
        myAppData.clear();
        myAppData.put(message.getBytes());
        myAppData.flip();
        while (myAppData.hasRemaining()) {
            myNetData.clear();
            SSLEngineResult result = engine.wrap(myAppData, myNetData);
            switch (result.getStatus()) {
            case OK:
                myNetData.flip();
                while (myNetData.hasRemaining()) {
                    socketChannel.write(myNetData);
                }
                break;
            case BUFFER_OVERFLOW:
                myNetData = enlargePacketBuffer(engine, myNetData);
                break;
            case BUFFER_UNDERFLOW:
                throw new SSLException(BUFOF_AFTER_WRAP_UNEXPECTED);
            case CLOSED:
                closeConnection(socketChannel, engine);
                return;
            default:
                throw new IllegalStateException(getSSLInvalidStatusMessage(result));
            }
        }
    }
}