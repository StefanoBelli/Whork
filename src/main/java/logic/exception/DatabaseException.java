package logic.exception;

public final class DatabaseException extends RuntimeException {
	private static final long serialVersionUID = 3606848904405050202L;
	
	public DatabaseException(String message) {
		super(message);
	}
}
