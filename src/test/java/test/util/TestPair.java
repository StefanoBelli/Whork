package test.util;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import logic.util.Pair;

public class TestPair {

	@Test
	public void testPair() {
		Object obj1 = new Object();
		Object obj2 = new Object();
		Pair<Object, Object> p = new Pair<>(obj1, obj2);

		assertTrue(obj1 == p.getFirst() &&  obj2 == p.getSecond());
	}
	
}
