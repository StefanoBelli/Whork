package logic.bean;

import java.io.Serializable;

import logic.exception.SyntaxException;

public final class UserAuthBean implements Serializable {
	private static final long serialVersionUID = 6053272591238153999L;
	
	private String email;
	private String password;

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) 
			throws SyntaxException {
		if(email.length() > 255) {
			throw new SyntaxException("Email length must be less or equal than 255 chars!");
		}

		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
