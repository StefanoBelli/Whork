package test.dao;

import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import logic.dao.JobPositionDao;
import logic.exception.DataAccessException;
import logic.pool.JobPositionPool;
import test.Db;

public class TestJobPositionDao {

	@BeforeClass
	public static void initDb() throws ClassNotFoundException, SQLException, DataAccessException {
		Db.init();
	}
	
	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		JobPositionDao.populatePool();
		assertNotEquals(0, JobPositionPool.getJobPositions().size());
	}
}
