package logic.exception;

public final class InvalidMethodSignatureException extends RuntimeException {
	private static final long serialVersionUID = -980001113678351898L;

	private final String name;

	public InvalidMethodSignatureException(String name) {
		this.name = name;
	}

	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();

		builder
			.append("\"")
			.append(name)
			.append("\" method signature must be: /* any-mods */ Response ")
			.append(name).append("(Request req) /* any-throws-decl */");

		return builder.toString();
	}
}
