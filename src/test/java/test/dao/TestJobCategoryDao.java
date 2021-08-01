package test.dao;

import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.JobCategoryDao;
import logic.pool.JobCategoryPool;
import test.Db;

public class TestJobCategoryDao {

	@BeforeClass
	public static void initDb() throws ClassNotFoundException, SQLException, DataAccessException {
		Db.init();
	}

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		JobCategoryDao.populatePool();
		assertNotEquals(0, JobCategoryPool.getJobCategories().size());
	}

}
