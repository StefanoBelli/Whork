package logic;

import logic.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
	public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	
	// Errors for DatabaseException. Stay away.
	private static final String ALREADY_CLOSED_MSG = 
			"Connection is already closed. This is an unexpected behavior.";
	private static final String UNINITIALIZED_MSG =
			"Database#getInstance(String,String,String) must be called for initialization.";
	
	private static Database instance;
	private Connection connection;
	
	private Database(String dbUrl) 
			throws ClassNotFoundException, SQLException {
		
		Class.forName(DRIVER);
		this.connection = DriverManager.getConnection(dbUrl);
	}
	
	public static Database getInstance(String hpPair, String username, String password) 
			throws ClassNotFoundException, SQLException {
		
		if(instance == null) {
			StringBuilder url = new StringBuilder()
					.append("jdbc:mysql://")
					.append(hpPair)
					.append("/?user=")
					.append(username);
			
			if(password != null) {
					url.append("&password=")
						.append(password);
			}

			url.append("&noAccessToProcedureBodies=true");

			url.append("&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");


			url.append("&useUnicode=true&useJDBCCompliantTimezoneShift=true");
			url.append("&useLegacyDatetimeCode=false&serverTimezone=UTC");
			
			instance = new Database(url.toString());
		}
		
		return instance;
	}
	
	public static Database getInstance() 
			throws DatabaseException {
		
		if(instance == null) {
			throw new DatabaseException(UNINITIALIZED_MSG);
		}
		
		return instance;
	}
	
	public Connection getConnection() 
		throws DatabaseException {
		
		try {
			if(connection.isClosed()) { //**CLOSED CONNECTION
				//cannot reopen connection at this point
				throw new DatabaseException(ALREADY_CLOSED_MSG);
			}
		} catch(SQLException e) { //**INVALID CONNECTION
			//cannot reopen connection at this point
			throw new DatabaseException(e.getMessage()); //Exception conversion
		}
		
		return connection;
	}
	
	public void closeConnection() 
		throws DatabaseException {
		
		try {
			if(!connection.isClosed()) {
				connection.close(); 
			} else { //**CLOSED CONNECTION
				//cannot reopen connection at this point
				throw new DatabaseException(ALREADY_CLOSED_MSG);
			}
		} catch(SQLException e) {//**INVALID CONNECTION
			//cannot reopen connection at this point
			throw new DatabaseException(e.getMessage()); //Exception conversion
		}
		
		//cannot reopen connection at this point
	}
}
