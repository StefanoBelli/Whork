package logic.util;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Util {
	private Util() {}

	private static final Pattern EMAIL_REGEX = 
		Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$", 
						Pattern.CASE_INSENSITIVE);
	
	public static void exceptionLog(Exception e) {
		Logger logger = LoggerFactory.getLogger("WhorkExceptionLogger");

		logger.error("***************************");
		logger.error("* EXCEPTION LOGGING START *");
		logger.error("***************************");
		logger.error("");

		StackTraceElement[] trace = Thread.currentThread().getStackTrace();

		StringBuilder builder = new StringBuilder();
		builder.append(trace[1].getMethodName())
			.append(" got called by: ")
			.append(trace[2].getClassName())
			.append("#")
			.append(trace[2].getMethodName())
			.append("()");

		logger.error(builder.toString());
		logger.error("");
		e.printStackTrace();
		logger.error("");
		logger.error("*************************");
		logger.error("* EXCEPTION LOGGING END *");
		logger.error("*************************");
	}

	public static boolean checkboxToBoolean(String value) {
		return value != null && value.equals("on");
	}

	public static boolean isValidEmail(String email) {
		return EMAIL_REGEX.matcher(email).matches();
	}
}
