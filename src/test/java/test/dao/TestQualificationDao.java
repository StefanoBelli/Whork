package test.dao;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.QualificationDao;
import logic.pool.QualificationPool;

public class TestQualificationDao {

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		QualificationDao.populatePool();
		assertNotEquals(0, QualificationPool.getQualifications().size());
	}

}
