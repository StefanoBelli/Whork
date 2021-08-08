package logic.exception;

/** 
 * @author Stefano Belli
 */
public final class AlreadyRegisteredCommandException extends RuntimeException {
	private static final long serialVersionUID = -9091076783347345111L;

	private final String msg;

	public AlreadyRegisteredCommandException(String cmd, String prev, String actual) {
		StringBuilder builder = new StringBuilder();

		builder
			.append("\"")
			.append(cmd)
			.append("\" - already registered with method: \"")
			.append(prev)
			.append("\", attempting registration with new method: \"")
			.append(actual)
			.append("\"");

		msg = builder.toString();
	}

	@Override
	public String getMessage() {
		return msg;
	}
}
