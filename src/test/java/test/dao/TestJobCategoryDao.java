package test.dao;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.JobCategoryDao;
import logic.pool.JobCategoryPool;

public class TestJobCategoryDao {

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		JobCategoryDao.populatePool();
		assertNotEquals(0, JobCategoryPool.getJobCategories().size());
	}

}
