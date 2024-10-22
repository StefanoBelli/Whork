package logic.exception;

/**
 * @author Magliari Elio
 */

public final class InvalidPasswordException extends Exception {
	
	private static final long serialVersionUID = -1911829347583854126L;
	private static final String MSG = "The password entered is not correct!";	
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
