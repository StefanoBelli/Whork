package test.dao;

import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.TypeOfContractDao;
import logic.pool.TypeOfContractPool;
import test.Db;

public class TestTypeOfContractDao {

	@BeforeClass
	public static void initDb() 
			throws ClassNotFoundException, SQLException, DataAccessException {
		Db.init();
	}

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		TypeOfContractDao.populatePool();
		assertNotEquals(0, TypeOfContractPool.getTypesOfContract().size());
	}

}
