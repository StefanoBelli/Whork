package logic.exception;

public final class InvalidMethodSignatureException extends RuntimeException {
	private static final long serialVersionUID = -980001113678351898L;

	private final String msg;

	public InvalidMethodSignatureException(String name) {
		StringBuilder builder = new StringBuilder();

		builder
			.append("\"")
			.append(name)
			.append("\" method signature must be: public /* any-mods */ Response ")
			.append(name)
			.append("(Request req) /* any-throws-decl */");

		msg = builder.toString();
	}

	@Override
	public String getMessage() {
		return msg;
	}
}
