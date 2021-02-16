package test.util;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import logic.util.Util;

public class TestUtil {

	@Test
	public void testCheckboxToBoolean() {
		boolean expectedTrue = Util.checkboxToBoolean("on");
		boolean expectedFalse = Util.checkboxToBoolean(null);
		boolean expectedFalse1 = Util.checkboxToBoolean("ON");
		boolean expectedFalse2 = Util.checkboxToBoolean("whatever");

		assertTrue(expectedTrue && 
			!expectedFalse && !expectedFalse1 && !expectedFalse2);
	}

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
	
}
