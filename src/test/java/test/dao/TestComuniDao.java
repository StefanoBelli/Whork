package test.dao;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.ComuniDao;
import logic.pool.ComuniPool;

public class TestComuniDao {

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		ComuniDao.populatePool();
		assertNotEquals(0, ComuniPool.getComuni().size());
	}
	
}
