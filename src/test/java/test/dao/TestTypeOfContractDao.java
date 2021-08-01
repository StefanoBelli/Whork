package test.dao;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.TypeOfContractDao;
import logic.pool.TypeOfContractPool;

public class TestTypeOfContractDao {

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		TypeOfContractDao.populatePool();
		assertNotEquals(0, TypeOfContractPool.getTypesOfContract().size());
	}

}
