package logic.exception;

import java.sql.SQLException;

public final class DataAccessException extends Exception {
	private static final long serialVersionUID = -1586073117767458937L;
	
	public DataAccessException(SQLException e) {
		super("Unable to read data", e);
	}
}
