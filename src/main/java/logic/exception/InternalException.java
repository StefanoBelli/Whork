package logic.exception;

/**
 * @author Michele Tosi
 */
public final class InternalException extends Exception {
	private static final long serialVersionUID = -1336211773882558616L;
	
	public InternalException(String msgToShow) {
		super(msgToShow);
	}
}
