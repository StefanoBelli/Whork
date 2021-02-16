package test.dao;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import logic.EmploymentsStatusPool;
import logic.dao.EmploymentStatusDao;
import logic.exception.DataAccessException;

public class TestEmploymentStatusDao {
	@BeforeClass
	public static void init() 
			throws DataAccessException {
		EmploymentStatusDao.populatePool();
	}

	@Test
	public void testEmployments() {
		assertEquals(10, EmploymentsStatusPool.getEmploymentsStatus().size());
	}
}
