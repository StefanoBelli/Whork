package logic.graphicscontroller.formchecker;

import java.util.List;

public final class JobSeekerFormCheckerDecorator extends FormCheckerDecorator {
	private final List<String> itTowns;

	public JobSeekerFormCheckerDecorator(FormChecker formChecker, List<String> itTowns) {
		super(formChecker);
		this.itTowns = itTowns;
	}

	/**
	 * decorates a BasicFormChecker object in order to fully-check a job seeker.
	 * Ensure correct types are placed into the array.
	 *  7 -> town
	 *  8 -> address
	 *  9 -> cv
	 *  10,11,12... -> [IGNORED] *DON'T APPLY ANY OTHER DECORATOR*
	 * 
	 * @param formEntries - form entries
	 * @return error string - if it is equal to "", form is good to go, nope otherwise
	 */
	@Override
	public String doChecks(Object[] formEntries) {
		StringBuilder errorBuilder = new StringBuilder(formChecker.doChecks(formEntries));

		if(s(formEntries[7]).isBlank()) {
			errorBuilder.append(" * Town field is blank\n");
		} else if(!findTown(s(formEntries[7]))) {
			errorBuilder.append(" * Town is not in the form we were expecting, use autocompletion\n");
		}

		if(s(formEntries[8]).isBlank()) {
			errorBuilder.append(" * Address field is blank\n");
		}

		if(formEntries[9] == null) {
			errorBuilder.append(" * You didn't choose your CV\n");
		}

		return errorBuilder.toString();
	}

	private boolean findTown(String town) {
		for(final String itTown : itTowns) {
			if(town.equals(itTown)) {
				return true;
			}
		}
		return false;
	}
}
