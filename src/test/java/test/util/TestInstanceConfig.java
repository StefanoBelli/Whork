package test.util;

import org.junit.Test;

import logic.util.Util;

import static org.junit.Assert.assertEquals;

public class TestInstanceConfig {

	@Test
	public void testNonPresentKey() {
		assertEquals(null, Util.InstanceConfig.get("nxkey"));
	}

	@Test
	public void testPresentKeyObject() {
		Object obj = new Object();
		Util.InstanceConfig.setConf("objkey", obj);
		assertEquals(obj, Util.InstanceConfig.get("objkey"));
	}
	
}
