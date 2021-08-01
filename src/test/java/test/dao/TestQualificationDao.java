package test.dao;

import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import logic.exception.DataAccessException;
import logic.dao.QualificationDao;
import logic.pool.QualificationPool;
import test.Db;

public class TestQualificationDao {

	@BeforeClass
	public static void initDb() throws ClassNotFoundException, SQLException, DataAccessException {
		Db.init();
	}

	@Test
	public void testPopulatePoolOk() throws DataAccessException {
		QualificationDao.populatePool();
		assertNotEquals(0, QualificationPool.getQualifications().size());
	}

}
