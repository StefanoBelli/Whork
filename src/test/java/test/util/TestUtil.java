package test.util;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

import logic.util.Util;

public class TestUtil {

	@Test
	public void testBcrypt1() {
		String clearPwd = "ciao123..";
		byte[] hash1 = Util.Bcrypt.hash(clearPwd);
		boolean verified = Util.Bcrypt.equals(clearPwd, hash1);

		assertTrue(hash1.length <= 60 && verified);
	}

	@Test
	public void testBcrypt2() {
		String clearPwd1 = "ciao123..";
		String clearPwd2 = "..ciao123";
		byte[] hash1 = Util.Bcrypt.hash(clearPwd1);
		boolean verified = Util.Bcrypt.equals(clearPwd2, hash1);

		assertTrue(hash1.length <= 60 && !verified);
	}

	@Test
	public void testGenerateToken() {
		assertEquals(50,Util.generateToken().length());
	}

	@Test
	public void testDeriveBirthdayFromFiscalCode() {
		Date d = Util.deriveBirthdayFromFiscalCode("BLLSFN99R08H501G");
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		assertTrue(
			c.get(Calendar.DAY_OF_MONTH) == 8 && 
			c.get(Calendar.MONTH) == 9 && //month indexing applies
			c.get(Calendar.YEAR) == 1999);
	}

	@Test
	public void testDeriveBirthdayFromFiscalCodeShouldBeWrongYear() {
		boolean passTest = false;
		try {
			Util.deriveBirthdayFromFiscalCode("BLLSFNABR13H501G");
		} catch(IllegalArgumentException e) {
			passTest = true;
		}

		assertTrue(passTest);
	}

	@Test
	public void testDeriveBirthdayFromFiscalCodeShouldBeWrongMonth() {
		boolean passTest = false;
		try {
			Util.deriveBirthdayFromFiscalCode("BLLSFN99Z13H501G");
		} catch (IllegalArgumentException e) {
			passTest = true;
		}

		assertTrue(passTest);
	}

	@Test
	public void testDeriveBirthdayFromFiscalCodeShouldBeWrongDay() {
		boolean passTest = false;
		try {
			Util.deriveBirthdayFromFiscalCode("BLLSFN99ZGGH501G");
		} catch (IllegalArgumentException e) {
			passTest = true;
		}

		assertTrue(passTest);
	}
}
