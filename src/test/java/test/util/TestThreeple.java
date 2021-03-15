package test.util;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import logic.util.Threeple;

public class TestThreeple {

	@Test
	public void testThreeple() {
		Object obj1 = new Object();
		Object obj2 = new Object();
		Object obj3 = new Object();
		Threeple<Object, Object, Object> p;
		p = new Threeple<>(obj1, obj2, obj3);

		assertTrue(obj1 == p.getFirst() && 
					obj2 == p.getSecond() && 
					obj3 == p.getThird());
	}

}
