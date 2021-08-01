package test.dao;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.EmploymentStatusDao;
import logic.pool.EmploymentsStatusPool;

public class TestEmploymentStatusDao {

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		EmploymentStatusDao.populatePool();
		assertNotEquals(0, EmploymentsStatusPool.getEmploymentsStatus().size());
	}

}
