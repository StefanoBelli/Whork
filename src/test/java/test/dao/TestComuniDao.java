package test.dao;

import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.ComuniDao;
import logic.pool.ComuniPool;
import test.Db;

public class TestComuniDao {

	@BeforeClass
	public static void initDb() 
			throws ClassNotFoundException, SQLException, DataAccessException {
		Db.init();
	}

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		ComuniDao.populatePool();
		assertNotEquals(0, ComuniPool.getComuni().size());
	}
	
}
