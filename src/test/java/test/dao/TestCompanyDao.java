package test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import logic.dao.CompanyDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.CompanyModel;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCompanyDao {
	@Test
	public void testARegisterCompany() 
			throws DataAccessException {
		CompanyModel model = new CompanyModel();
		model.setCf("0123456789ABCDEF");
		model.setLogo(null);
		model.setSocialReason("Impianti idroelettrici");
		model.setVat("12345678900");
		CompanyDao.registerCompany(model);

		assertTrue(true);
	}

	@Test
	public void testBGetCompanyShouldFail() 
			throws DataAccessException, DataLogicException {
		assertEquals(null, CompanyDao.getCompanyByVat("11111111111"));
	}

	@Test
	public void testCGetCompanyShouldOk() 
			throws DataAccessException, DataLogicException {
		assertNotEquals(null, CompanyDao.getCompanyByVat("12345678900"));
	}

}
