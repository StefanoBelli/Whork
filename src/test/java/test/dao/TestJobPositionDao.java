package test.dao;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import logic.dao.JobPositionDao;
import logic.exception.DataAccessException;
import logic.pool.JobPositionPool;

public class TestJobPositionDao {
	
	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		JobPositionDao.populatePool();
		assertNotEquals(0, JobPositionPool.getJobPositions().size());
	}
}
