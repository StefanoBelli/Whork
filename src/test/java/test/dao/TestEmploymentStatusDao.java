package test.dao;

import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.EmploymentStatusDao;
import logic.pool.EmploymentsStatusPool;
import test.Db;

public class TestEmploymentStatusDao {

	@BeforeClass
	public static void initDb() throws ClassNotFoundException, SQLException, DataAccessException {
		Db.init();
	}

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		EmploymentStatusDao.populatePool();
		assertNotEquals(0, EmploymentsStatusPool.getEmploymentsStatus().size());
	}

}
