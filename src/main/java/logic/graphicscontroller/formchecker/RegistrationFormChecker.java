package logic.graphicscontroller.formchecker;

import logic.util.Util;

public final class RegistrationFormChecker extends FormChecker {
	/**
	 * caller has responsibility to ensure correct entry placement within the raw array,
	 * to match doChecks' ordering. 
	 * Ensure correct types are placed into the array.
	 *  0 -> email
	 *  1 -> password
	 *  2 -> retyped password
	 *  3 -> name
	 *  4 -> surname
	 *  5 -> fiscal code
	 *  6 -> phone number
	 *  7,8,... -> [IGNORED], see decorators.
	 * 
	 * @param formEntries - form entries
	 * @return error string - if it is equal to "", form is good to go, nope otherwise
	 */
	@Override
	public String doChecks(Object[] formEntries) {
		StringBuilder errorBuilder = new StringBuilder("");

		if (s(formEntries[0]).isBlank()) {
			errorBuilder.append(" * Email field is blank\n");
		} else if (!Util.EMAIL_PATTERN.matcher(s(formEntries[0])).matches()) {
			errorBuilder.append(" * Email is not a valid one\n");
		}

		if (s(formEntries[1]).isBlank() || s(formEntries[2]).isBlank()) {
			errorBuilder.append(" * Password and/or retype password fields are blank\n");
		} else if (!s(formEntries[1]).equals(s(formEntries[2]))) {
			errorBuilder.append(" * Password is not matching the retyped one\n");
		}

		if (s(formEntries[3]).isBlank()) {
			errorBuilder.append(" * Name field is blank\n");
		}

		if (s(formEntries[4]).isBlank()) {
			errorBuilder.append(" * Surname field is blank\n");
		}

		if (s(formEntries[5]).isBlank()) {
			errorBuilder.append(" * Fiscal code field is blank\n");
		} else if (!Util.FC_PATTERN.matcher(s(formEntries[5])).matches()) {
			errorBuilder.append(" * Fiscal code is not a valid one\n");
		}

		if (s(formEntries[6]).isBlank()) {
			errorBuilder.append(" * Phone number field is blank\n");
		} else if (s(formEntries[6]).length() < 9) {
			errorBuilder.append(" * Phone number length is less than 9\n");
		}

		return errorBuilder.toString();
	}
}
