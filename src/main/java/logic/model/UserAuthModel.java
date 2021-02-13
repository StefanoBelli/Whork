package logic.model;

import java.io.ByteArrayInputStream;

public class UserAuthModel {
	private String email;
	private ByteArrayInputStream bcryptedPassword;

	public String getEmail() {
		return email;
	}

	public ByteArrayInputStream getBcryptedPassword() {
		return bcryptedPassword;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setBcryptedPassword(ByteArrayInputStream bcryptedPassword) {
		this.bcryptedPassword = bcryptedPassword;
	}
}
