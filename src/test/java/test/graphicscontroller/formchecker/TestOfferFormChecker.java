package test.graphicscontroller.formchecker;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import logic.graphicscontroller.formchecker.OfferFormChecker;

/**
 * @author Michele Tosi
 */
public class TestOfferFormChecker {
	private static final String SELECT_AN_OPTION = "--select an option--";
	
	@Test
	public void testOfferFormChecker() {
		OfferFormChecker offerFormChecker = new OfferFormChecker();
		Object[] strings =new Object[7];
		strings[0]="";
		strings[1]="13 - 14";
		strings[2]=SELECT_AN_OPTION;
		strings[3]=SELECT_AN_OPTION;
		strings[4]=SELECT_AN_OPTION;
		strings[5]=SELECT_AN_OPTION;
		strings[6]="";
		assertEquals(offerFormChecker.doChecks(strings)," * Offer name field is blank\n"
				+ " * Work shift is not a valid one\n"
				+ " * Select an option for category\n"
				+ " * Select an option for position\n"
				+ " * Select an option for qualification\n"
				+ " * Select an option for type of contract\n"
				+ " * Description field is blank\n" );
	}

}
