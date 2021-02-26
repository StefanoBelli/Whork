package logic.controller;

public final class MailSender {
	private String from = null;
	private String password = null;
	private String host = null;
	private String port = null;
	private String tls = null;

	public void setFrom(String from) {
		this.from = from;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setTls(String tls) {
		this.tls = tls;
	}

	public String getFrom() {
		return from;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}
	
	public String getPort() {
		return port;
	}

	public String getTls() {
		return tls;
	}
}
