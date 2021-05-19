package logic.exception;

public final class AlreadyRegisteredCommandException extends RuntimeException {
	private static final long serialVersionUID = -9091076783347345111L;

	private final String cmd;
	private final String prev;
	private final String actual;

	public AlreadyRegisteredCommandException(String cmd, String prev, String actual) {
		this.cmd = cmd;
		this.prev = prev;
		this.actual = actual;
	}

	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		
		builder
			.append("\"")
			.append(cmd)
			.append("\" - already registered with method: \"")
			.append(prev)
			.append("\", attempting registration with new method: \"")
			.append(actual)
			.append("\"");

		return builder.toString();
	}
}
