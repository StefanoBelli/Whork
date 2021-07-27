package logic.exception;

public final class InvalidPasswordException extends RuntimeException {
	
	private static final long serialVersionUID = -1911829347583854126L;
	private static final String msg = "The password entered is not correct!";	
	
	@Override
	public String getMessage() {
		return msg;
	}
	
}
