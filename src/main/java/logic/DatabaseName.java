package logic;

final class DatabaseName {
	private static String dbName = null;
	
	private DatabaseName() {}
	
	public static void setDbName(String dbName) {
		DatabaseName.dbName = dbName;
	}
	
	public static String getDbName() 
			throws DatabaseNameException {
		
		if(DatabaseName.dbName == null) {
			throw new DatabaseNameException();
		}
		
		return DatabaseName.dbName;
	}
}
