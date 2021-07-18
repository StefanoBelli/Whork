package logic.bean;

import java.io.Serializable;

public final class ChatInitBean implements Serializable {
	private static final long serialVersionUID = 679762904459557489L;

	private String token;
	private int tokenExpiresIn;
	private int shouldPullMessagesEvery;
	private int servicePort;

	public String getToken() {
		return token;
	}

	public int getTokenExpiresIn() {
		return tokenExpiresIn;
	}

	public int getShouldPullMessagesEvery() {
		return shouldPullMessagesEvery;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setTokenExpiresIn(int tokenExpiresIn) {
		this.tokenExpiresIn = tokenExpiresIn;
	}

	public void setShouldPullMessagesEvery(int shouldPullMessagesEvery) {
		this.shouldPullMessagesEvery = shouldPullMessagesEvery;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}
}
