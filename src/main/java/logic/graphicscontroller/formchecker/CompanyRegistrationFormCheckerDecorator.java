package logic.graphicscontroller.formchecker;

import logic.util.Util;

/**
 * @author Stefano Belli
 */
public final class CompanyRegistrationFormCheckerDecorator  extends FormCheckerDecorator {
	public CompanyRegistrationFormCheckerDecorator(FormChecker formChecker) {
		super(formChecker);
	}

	/**
	 * decorates a BasicFormChecker object in order to fully-check a company.
	 * Ensure correct types are placed into the array.
	 *  7 -> business name
	 *  8 -> VAT no
	 *  9 -> company fiscal code
	 *  10 -> company logo
	 *  11,12... -> [IGNORED] *DON'T APPLY ANY OTHER DECORATOR*
	 * 
	 * @param formEntries - form entries
	 * @return error string - if it is equal to "", form is good to go, nope otherwise
	 */
	@Override
	public String doChecks(Object[] formEntries) {
		StringBuilder errorBuilder = new StringBuilder(formChecker.doChecks(formEntries));

		if(s(formEntries[7]).isBlank()) {
			errorBuilder.append(" * Business name field is blank\n");
		} else if(s(formEntries[7]).length() > 45) {
			errorBuilder.append(" * Business name is longer than 45 chars\n");
		}

		if(s(formEntries[8]).isBlank()) {
			errorBuilder.append(" * VAT number field is blank\n");
		} else if(s(formEntries[8]).length() < 11) {
			errorBuilder.append(" * VAT number length is less than 11 chars\n");
		}

		if(s(formEntries[9]).isBlank()) {
			errorBuilder.append(" * Company fiscal code field is blank\n");
		} else if(!Util.FC_PATTERN.matcher(s(formEntries[9])).matches()) {
			errorBuilder.append(" * Company fiscal code is not a valid one\n");
		}

		if(formEntries[10] == null) {
			errorBuilder.append(" * You didn't choose your company logo\n");
		}

		return errorBuilder.toString();
	}
}
