package test.util;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import logic.util.ServletUtil;

public class TestServletUtil {
	@Test
	public void testCheckboxToBoolean() {
		boolean expectedTrue = ServletUtil.checkboxToBoolean("on");
		boolean expectedFalse = ServletUtil.checkboxToBoolean(null);
		boolean expectedFalse1 = ServletUtil.checkboxToBoolean("ON");
		boolean expectedFalse2 = ServletUtil.checkboxToBoolean("whatever");

		assertTrue(expectedTrue && !expectedFalse && !expectedFalse1 && !expectedFalse2);
	}
}
