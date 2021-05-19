package logic.bean;

import java.io.Serializable;

public final class ChatInitBean implements Serializable {
	private String token;
	private int tokenExpiresIn;
	private int shouldPullMessagesEvery;

	public String getToken() {
		return token;
	}

	public int getTokenExpiresIn() {
		return tokenExpiresIn;
	}

	public int getShouldPullMessagesEvery() {
		return shouldPullMessagesEvery;
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
}
