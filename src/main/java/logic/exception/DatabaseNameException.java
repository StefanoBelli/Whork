package logic.exception;

public final class DatabaseNameException extends RuntimeException {
	private static final long serialVersionUID = -2223458074648370214L;
	
	private static final String MSG =
		"DatabaseName#setDbName(String) must be called, setting a DB name different than null";

	public DatabaseNameException() {
		super(MSG);
	}
}
