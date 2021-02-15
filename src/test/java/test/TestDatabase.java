package test;

import logic.Database;
import logic.exception.DatabaseException;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDatabase {
	@Test
	public void testADatabaseShouldThrowEx() {
		assertThrows(DatabaseException.class, () -> {
			Database.getInstance();
		});
	}

	@Test
	public void testBDatabaseShouldWork() 
			throws ClassNotFoundException, SQLException {

		Database db = Database.getInstance(
			DbmsConfig.DB_HOST + ":" + Integer.toString(DbmsConfig.DB_PORT),
			DbmsConfig.DB_USER, DbmsConfig.DB_PWD);

		assertNotEquals(null, db);
	}

	@Test
	public void testCDatabaseShouldWork() {
		assertNotEquals(null, Database.getInstance());
	}

	@Test
	public void testDDatabaseShouldWork() {
		assertNotEquals(null, Database.getInstance().getConnection());
	}

	@Test
	public void testEDatabaseShouldWork() 
			throws ClassNotFoundException, SQLException {

		Database db = Database.getInstance(
				DbmsConfig.DB_HOST + ":" + Integer.toString(DbmsConfig.DB_PORT),
				DbmsConfig.DB_USER, DbmsConfig.DB_PWD);

		assertNotEquals(null, db.getConnection());
	}

	@AfterClass
	public static void chooseDb() 
			throws SQLException {
				
		Database.getInstance().getConnection().setCatalog(DbmsConfig.DB_NAME);
	}
}