package logic.net.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import logic.util.Util.Holder;

abstract class SSLPeer {
	private static final String INVALID_SSL_STATUS = "Invalid SSL status: ";
	private static final String KEYSTORE_TYPE = "JKS";
	protected static final String BUFOF_AFTER_WRAP_UNEXPECTED = "Buffer underflow occured after a wrap (this is unexpected)";

	protected ByteBuffer myAppData;
	protected ByteBuffer myNetData;
	protected ByteBuffer peerAppData;
	protected ByteBuffer peerNetData;

	protected ExecutorService executor = Executors.newSingleThreadExecutor();

	protected abstract void read(SocketChannel socketChannel, SSLEngine engine) throws IOException;

	protected abstract void write(SocketChannel socketChannel, SSLEngine engine, String message) throws IOException;

	protected final boolean doHandshake(SocketChannel socketChannel, SSLEngine engine) throws IOException {
		Holder<HandshakeStatus> handshakeStatusHolder = new Holder<>();
		Holder<ByteBuffer> thePeerAppDataHolder = new Holder<>();

		int appBufferSize = engine.getSession().getApplicationBufferSize();
		ByteBuffer theMyAppData = ByteBuffer.allocate(appBufferSize);
		ByteBuffer thePeerAppData = ByteBuffer.allocate(appBufferSize);
		myNetData.clear();
		peerNetData.clear();

		HandshakeStatus handshakeStatus = engine.getHandshakeStatus();
		handshakeStatusHolder.setHeldObject(handshakeStatus);
		thePeerAppDataHolder.setHeldObject(thePeerAppData);

		while (handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED
				&& handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {

			switch (handshakeStatus) {
				case NEED_UNWRAP:
					if (!unwrap(thePeerAppDataHolder, engine, handshakeStatusHolder, socketChannel)) {
						return false;
					}
					break;
				case NEED_WRAP:
					wrap(theMyAppData, engine, handshakeStatusHolder, socketChannel);
					break;
				case NEED_TASK:
					task(engine, handshakeStatusHolder);
					break;
				case FINISHED:
					break;
				case NOT_HANDSHAKING:
					break;
				default:
					throw new IllegalStateException(getSSLInvalidStatusMessage(handshakeStatus));
			}

			handshakeStatus = handshakeStatusHolder.getHeldObject();
		}

		return true;
	}

	protected final ByteBuffer enlargePacketBuffer(SSLEngine engine, ByteBuffer buffer) {
		return enlargeBuffer(buffer, engine.getSession().getPacketBufferSize());
	}

	protected final ByteBuffer enlargeApplicationBuffer(SSLEngine engine, ByteBuffer buffer) {
		return enlargeBuffer(buffer, engine.getSession().getApplicationBufferSize());
	}

	protected final ByteBuffer enlargeBuffer(ByteBuffer buffer, int sessionProposedCapacity) {
		if (sessionProposedCapacity > buffer.capacity()) {
			buffer = ByteBuffer.allocate(sessionProposedCapacity);
		} else {
			buffer = ByteBuffer.allocate(buffer.capacity() * 2);
		}

		return buffer;
	}

	protected final ByteBuffer handleBufferUnderflow(SSLEngine engine, ByteBuffer buffer) {
		if (engine.getSession().getPacketBufferSize() < buffer.limit()) {
			return buffer;
		}

		ByteBuffer replaceBuffer = enlargePacketBuffer(engine, buffer);
		buffer.flip();
		replaceBuffer.put(buffer);

		return replaceBuffer;
	}

	protected final void closeConnection(SocketChannel socketChannel, SSLEngine engine) throws IOException {
		engine.closeOutbound();
		doHandshake(socketChannel, engine);
		socketChannel.close();
	}

	protected final void handleEndOfStream(SocketChannel socketChannel, SSLEngine engine) throws IOException {
		try {
			engine.closeInbound();
		} catch (Exception e) {
			// unhandled
		}

		closeConnection(socketChannel, engine);
	}

	protected final KeyManager[] createKeyManagers(String filepath, String keystorePassword, String keyPassword)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);

		try (InputStream keyStoreIS = new FileInputStream(filepath)) {
			keyStore.load(keyStoreIS, keystorePassword.toCharArray());
		}

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, keyPassword.toCharArray());
		return kmf.getKeyManagers();
	}

	protected final TrustManager[] createTrustManagers(String filepath, String keystorePassword) 
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);

		try (InputStream trustStoreIS = new FileInputStream(filepath)) {
			trustStore.load(trustStoreIS, keystorePassword.toCharArray());
		}

		TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustFactory.init(trustStore);
		return trustFactory.getTrustManagers();
	}

	protected String getSSLInvalidStatusMessage(SSLEngineResult result) {
		return new StringBuilder().append(INVALID_SSL_STATUS).append(result.getStatus()).toString();
	}

	private String getSSLInvalidStatusMessage(HandshakeStatus status) {
		return new StringBuilder().append(INVALID_SSL_STATUS).append(status).toString();
	}

	private void wrap(ByteBuffer theMyAppData, SSLEngine engine, Holder<HandshakeStatus> handshakeStatusHolder,
			SocketChannel socketChannel) throws IOException {
		myNetData.clear();
		SSLEngineResult result;

		try {
			result = engine.wrap(theMyAppData, myNetData);
			handshakeStatusHolder.setHeldObject(result.getHandshakeStatus());
		} catch (SSLException sslException) {
			engine.closeOutbound();
			handshakeStatusHolder.setHeldObject(engine.getHandshakeStatus());
			return;
		}

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
				try {
					myNetData.flip();
					while (myNetData.hasRemaining()) {
						socketChannel.write(myNetData);
					}
					peerNetData.clear();
				} catch (Exception e) {
					handshakeStatusHolder.setHeldObject(engine.getHandshakeStatus());
				}
				break;
			default:
				throw new IllegalStateException(getSSLInvalidStatusMessage(result));
		}
	}

	private boolean unwrap(Holder<ByteBuffer> thePeerAppDataHolder, SSLEngine engine, Holder<HandshakeStatus> handshakeStatusHolder,
			SocketChannel socketChannel) throws IOException {
		if (socketChannel.read(peerNetData) < 0) {
			if (engine.isInboundDone() && engine.isOutboundDone()) {
				return false;
			}

			try {
				engine.closeInbound();
			} catch (SSLException e) {
				// unhandled
			}

			engine.closeOutbound();
			handshakeStatusHolder.setHeldObject(engine.getHandshakeStatus());
			return true;
		}

		peerNetData.flip();
		SSLEngineResult result;

		try {
			result = engine.unwrap(peerNetData, thePeerAppDataHolder.getHeldObject());
			peerNetData.compact();
			handshakeStatusHolder.setHeldObject(result.getHandshakeStatus());
		} catch (SSLException sslException) {
			engine.closeOutbound();
			handshakeStatusHolder.setHeldObject(engine.getHandshakeStatus());
			return true;
		}

		switch (result.getStatus()) {
			case OK:
				break;
			case BUFFER_OVERFLOW:
				thePeerAppDataHolder.setHeldObject(enlargeApplicationBuffer(engine, thePeerAppDataHolder.getHeldObject())); //FIXFIXFIX TODO //TODO //TODO
				break;
			case BUFFER_UNDERFLOW:
				peerNetData = handleBufferUnderflow(engine, peerNetData);
				break;
			case CLOSED:
				if (engine.isOutboundDone()) {
					return false;
				} else {
					engine.closeOutbound();
					handshakeStatusHolder.setHeldObject(engine.getHandshakeStatus());
					break;
				}
			default:
				throw new IllegalStateException(getSSLInvalidStatusMessage(result));
		}

		return true;
	}

	private void task(SSLEngine engine, Holder<HandshakeStatus> handshakeStatusHolder) {
		Runnable task;
		while ((task = engine.getDelegatedTask()) != null) {
			executor.execute(task);
		}

		handshakeStatusHolder.setHeldObject(engine.getHandshakeStatus());
	}
}