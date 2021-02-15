package test.dao;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import logic.ComuniPool;
import logic.dao.ComuniDao;
import logic.exception.DataAccessException;

public class TestComuniDao {
	@BeforeClass
	public static void init() throws DataAccessException {
		ComuniDao.populatePool();
	}

	@Test
	public void testComuni() {
		assertEquals(8019, ComuniPool.getComuni().size());
	}

	@Test
	public void testProvince() {
		assertEquals(107, ComuniPool.getProvince().size());
	}

	@Test
	public void testRegioni() {
		assertEquals(20, ComuniPool.getRegioni().size());
	}

}
