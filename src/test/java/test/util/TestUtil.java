package test.util;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

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
	
}
