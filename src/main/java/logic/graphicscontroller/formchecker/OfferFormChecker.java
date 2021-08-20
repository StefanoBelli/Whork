package logic.graphicscontroller.formchecker;

import logic.util.Util;

/**
 * @author Michele Tosi
 */
public final class OfferFormChecker extends FormChecker{
	private static final String SELECT_AN_OPTION = "--select an option--";
	
	@Override
	public String doChecks(Object[] formEntries) {
		StringBuilder errorBuilder = new StringBuilder("");
		
		if(s(formEntries[0]).isBlank()) {
			errorBuilder.append(" * Offer name field is blank\n");
		}
		
		if(!Util.WORK_SHIFT_PATTERN.matcher(s(formEntries[1])).matches()){
			errorBuilder.append(" * Work shift is not a valid one\n");
		}		
		
		if(s(formEntries[2]).equals(SELECT_AN_OPTION)) {
			errorBuilder.append(" * Select an option for category\n");
		}
		
		if(s(formEntries[3]).equals(SELECT_AN_OPTION)) {
			errorBuilder.append(" * Select an option for position\n");
		}
		
		if(s(formEntries[4]).equals(SELECT_AN_OPTION)) {
			errorBuilder.append(" * Select an option for qualification\n");
		}
		
		if(s(formEntries[5]).equals(SELECT_AN_OPTION)) {
			errorBuilder.append(" * Select an option for type of contract\n");
		}
		
		if(s(formEntries[6]).isBlank()) {
			errorBuilder.append(" * Description field is blank\n");
		}
		return errorBuilder.toString();
	}

}
