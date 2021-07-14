package logic.graphicscontroller.formchecker;

public abstract class FormChecker {
	public abstract String doChecks(Object[] formEntries);

	/**
	 * caller responsibility is to ensure correct "runtime" type of the 
	 * object o being passed to the utility method.
	 * 
	 * @param o object to be casted
	 * @return o object casted from java.lang.Object to java.lang.String
	 */
	protected static String s(Object o) {
		return (String) o;
	}
}
