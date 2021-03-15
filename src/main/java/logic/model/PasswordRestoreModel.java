package logic.model;

import java.util.Date;

public final class PasswordRestoreModel {
	private String email;
	private String token;
	private Date date;

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getEmail() {
		return email;
	}

	public String getToken() {
		return token;
	}

	public Date getDate() {
		return date;
	}
}
