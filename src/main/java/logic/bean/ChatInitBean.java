package logic.bean;

import java.io.Serializable;

public final class ChatInitBean implements Serializable {
	private String token;
	private int expiresIn;
	private int pullMessageEvery;
	private int refreshTokenEvery;

	public String getToken() {
		return token;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public int getPullMessageEvery() {
		return pullMessageEvery;
	}

	public int getRefreshTokenEvery() {
		return refreshTokenEvery;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public void setPullMessageEvery(int pullMessageEvery) {
		this.pullMessageEvery = pullMessageEvery;
	}

	public void setRefreshTokenEvery(int refreshTokenEvery) {
		this.refreshTokenEvery = refreshTokenEvery;
	}
}
